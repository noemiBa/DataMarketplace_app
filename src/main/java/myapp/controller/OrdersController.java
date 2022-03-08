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
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.util.List;

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

    @GetMapping("/order_paid")
    public String createOrder(Model model) {
        Users user = activeUser.getInstance().getActiveUser();
        List<CartItem> cartItems = cartServices.listCartItems(user);
        Double totPrice = cartServices.getTotalPrice(user);
        //Integer totQuantity = cartServices.getTotalQuantity(user);

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("New");
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        order.setDate(sqlDate);
        order.setTotPrice(totPrice.floatValue());
        ordersRepo.save(order);

        for (int i = 0; i<cartItems.size(); i++) {
            Order_items item = new Order_items();
            item.setOrder(order);
            item.setProduct(cartItems.get(i).getProduct());
            item.setItemNumber(i);
            item.setQuantity(cartItems.get(i).getQuantity());
            item.setPrice(cartItems.get(i).getItemCostByQuantity().floatValue());
            order_itemsRepo.save(item);
        }

        //NEED TO EMPTY SHOPPING CART TABLE FOR USER HERE. 

        return "shoppingcart.html";
    }

}
