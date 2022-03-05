package myapp.controller;

import myapp.model.Data_assets;
import myapp.repository.CartItemRepository;
import myapp.repository.Data_assetsRepository;
import myapp.model.CartItem;
import myapp.model.Users;
import myapp.model.activeUser;
import myapp.services.ShoppingCartServices;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import javax.servlet.http.HttpServletResponse;


@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServices cartServices;
    @Autowired
    private Data_assetsRepository dataRepo;
    @Autowired
    private CartItemRepository cartRepo;

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
            model.addAttribute("goHereAfterLogin","/shoppingcart");
            return "login.html";
        }
    }

    @PostMapping("/add_data_cart")
    public @ResponseBody Integer addToCard(
                                        @RequestParam Integer qty,
                                        @RequestParam Integer datasetID,
                                        HttpServletResponse response) throws Exception {

        //if(!activeUser.getInstance().isActiveUserLoggedIn()) {
            Users user = activeUser.getInstance().getActiveUser();

            Integer addedQuantity = qty;

            Data_assets asset = dataRepo.findById(datasetID).get();
            CartItem cartItem = cartRepo.findByUserAndProduct(user, asset);

            if (cartItem != null) { //check if item is already in cart
                addedQuantity = cartItem.getQuantity() + qty;
                cartItem.setQuantity(addedQuantity);
            } else {
                cartItem = new CartItem();
                cartItem.setProduct(asset);
                cartItem.setUser(user);
                cartItem.setQuantity(qty);
            }

            cartRepo.save(cartItem);
            response.sendRedirect("/shoppingcart");
        //}
        return addedQuantity;
    }
    
}
