package myapp.controller;

import myapp.model.CartItem;
import myapp.model.Users;
import myapp.model.activeUser;
import myapp.services.ShoppingCartServices;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServices cartServices;
    private boolean goToCartAfterLogin = false;

    @GetMapping("/shoppingcart")
    public String shoppingcart(Model model) {
        if(!activeUser.getInstance().isActiveUserLoggedIn()) {
            Users user = activeUser.getInstance().getActiveUser();
            List<CartItem> cartItems = cartServices.listCartItems(user);
            Double totPrice = cartServices.getTotalPrice(user);
            Integer totQuantity = cartServices.getTotalQuantity(user);

            //user items to add to page
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totPrice", totPrice);
            model.addAttribute("totQuantity", totQuantity);

            return "shoppingcart.html";
        } else {
            goToCartAfterLogin = true;
            return "login.html";
        }
    }
}
