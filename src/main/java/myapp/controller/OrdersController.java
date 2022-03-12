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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

}
