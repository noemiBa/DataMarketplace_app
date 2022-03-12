package myapp.controller;

import myapp.model.*;
import myapp.repository.CartItemRepository;
import myapp.repository.Data_assetsRepository;
import myapp.repository.Order_itemsRepository;
import myapp.repository.OrdersRepository;
import myapp.services.ShoppingCartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.core.io.FileSystemResource;

@Controller
public class OrdersController {

    @Autowired
    private ShoppingCartServices cartServices;

    @Autowired
    private Data_assetsRepository dataRepo;

    @Autowired
    private CartItemRepository cartRepo;

    @Autowired
    private OrdersRepository ordersRepo;

    @Autowired
    private Order_itemsRepository order_itemsRepo;

    @Transactional
    @GetMapping("/order_paid")
    public String createOrder(Model model) {
        Users user = activeUser.getInstance().getActiveUser();
        List<CartItem> cartItems = cartServices.listCartItems(user);
        Double totPrice = cartServices.getTotalPrice(user);

        if(activeUser.getInstance().isActiveUserLoggedIn()){
            model.addAttribute("loginRouting","/login");
            model.addAttribute("loginstate","Login");
            model.addAttribute("loggedIn", false);
        } else {
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
            model.addAttribute("loggedIn", true);
        }

        //Add to Orders table
        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("New");
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        order.setDate(sqlDate);
        order.setTotPrice(totPrice.floatValue());
        ordersRepo.save(order);

        //Add to order_items table
        for (int i = 0; i<cartItems.size(); i++) {
            Order_items item = new Order_items();
            item.setOrder(order);
            item.setProduct(cartItems.get(i).getProduct());
            item.setItemNumber(i);
            item.setQuantity(cartItems.get(i).getQuantity());
            item.setPrice(cartItems.get(i).getItemCostByQuantity().floatValue());
            order_itemsRepo.save(item);
        }

        // Create physical order
        int orderID = order.getOrder_id();
        ArrayList<Dataset> orderJson = new ArrayList<>();
        for (int i = 0; i < cartItems.size(); i++) {
            Data_assets asset = cartItems.get(i).getProduct();
            Dataset set = new Dataset();
            // Load dataset with asset name
            set = Dataset.json2Java(asset.getAssetname().toLowerCase()+".json", Dataset.class);
            // Randomise the values
            Collections.shuffle(set.getValues());
            // Select the first n where n is qty ordered
            set.setValues(new ArrayList<Double>(set.getValues().subList(0, cartItems.get(i).getQuantity())));
            orderJson.add(set);
            //System.out.println("Asset ordered = " + asset.getAssetname());
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
			FileWriter fw = new FileWriter("src/main/resources/static/orders/order" + orderID + ".json");
			gson.toJson(orderJson, fw);
			fw.close();
		} catch(IOException ie) {System.out.println("Error opening file");}
        //System.out.println("Order num: " + orderID);

        //Delete all cart items for active user after payment received.
        cartRepo.deleteByCustomer(user.getId());

        return "thankyou.html";
    }

    @Transactional
    @PostMapping("/change_order_status")
    public @ResponseBody void changeOrderStatus(@RequestParam Integer orderID,
                                                @RequestParam String newStatus,
                                                @RequestParam Integer userID,
                                                HttpServletResponse response) throws Exception {

//        Users user = activeUser.getInstance().getActiveUser();

        ordersRepo.updateStatus(newStatus, orderID, userID);

        response.sendRedirect("/viewallorders");
    }

    @GetMapping("/view_order/{id}")
    public String viewData(@PathVariable("id") String dataId, Model model) {
        // Setup Links for login/ out
        if(activeUser.getInstance().isActiveUserLoggedIn()){
            model.addAttribute("loginRouting","/login");
            model.addAttribute("loginstate","Login");
            model.addAttribute("loggedIn", false);
        } else {
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
            model.addAttribute("loggedIn", true);
        }
        int idNum;
        // Convert id to number. If not a number return non existant view
        try {
            idNum = Integer.parseInt(dataId);
        } catch(NumberFormatException nfe) {
            model.addAttribute("exists", false);
            return "view_order.html";
        }

        Optional<Orders> orderOption = ordersRepo.findById(idNum);
        Orders order;
        try {
            order = orderOption.get();
        } catch(NoSuchElementException nsee) {
            model.addAttribute("exists", false);
            return "view_order.html";
        }

        List<Order_items> orderItems = order_itemsRepo.listOrderItemsByOrder(order);
        Integer totQuantity = orderItems.stream().mapToInt(item -> item.getQuantity()).sum();

        model.addAttribute("exists", true);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totQuantity", totQuantity);

        if(activeUser.getInstance().getActiveUser().isAdmin()) {
            model.addAttribute("admin", true);
        } else {
            model.addAttribute("admin", false);
        }

        return "view_order.html";
    }

    @GetMapping("/viewallorders")
    public String viewAllOrders(Model model) {
        // send down types for filtering and assets
        Users user = activeUser.getInstance().getActiveUser();
        ArrayList<String> orderStats = new ArrayList<>();
        orderStats.add("New"); orderStats.add("Cancelled"); orderStats.add("Fullfilled");

        model.addAttribute("orders", ordersRepo.findAll());
        model.addAttribute("ordersByCustomer", ordersRepo.findByUser(user));
        model.addAttribute("orderStats", orderStats);

        if(activeUser.getInstance().isActiveUserLoggedIn()){
            model.addAttribute("loginRouting","/login");
            model.addAttribute("loginstate","Login");
            model.addAttribute("loggedIn", false);
        } else {
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
            model.addAttribute("loggedIn", true);
            if(activeUser.getInstance().getActiveUser().isAdmin()) {
                model.addAttribute("admin", true);
            } else {
                model.addAttribute("admin", false);
            }
        }

        return "view_all_orders.html";
    }

    @RequestMapping(value="/download_order/", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(@RequestParam(value="id") int id) {
        return new FileSystemResource("src/main/resources/static/orders/order" + id +".json");
    } 

}
