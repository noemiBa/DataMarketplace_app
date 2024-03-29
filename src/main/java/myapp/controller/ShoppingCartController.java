package myapp.controller;

import myapp.model.Data_assets;
import myapp.repository.CartItemRepository;
import myapp.repository.Data_assetsRepository;
import myapp.model.CartItem;
import myapp.model.Users;
import myapp.model.activeUser;
import myapp.services.ShoppingCartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;


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

            if(activeUser.getInstance().isActiveUserLoggedIn()){
                model.addAttribute("loginRouting","/login");
                model.addAttribute("loginstate","Login");
                model.addAttribute("loggedIn", false);
            } else {
                model.addAttribute("loginRouting","/logout");
                model.addAttribute("loginstate","Log Out");
                model.addAttribute("loggedIn", true);
            }

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
        return addedQuantity;
    }

    @Transactional
    @RequestMapping(value="/update_cart",params="update",method=RequestMethod.POST)
    public @ResponseBody String updateQuantity(
            @RequestParam Integer qty,
            @RequestParam Integer productID,
            HttpServletResponse response) throws Exception {

        Users user = activeUser.getInstance().getActiveUser();

        float subtotal = cartServices.updateQuantity(productID, qty, user);

        Data_assets asset = dataRepo.findById(productID).get();
        CartItem cartItem = cartRepo.findByUserAndProduct(user, asset);
        cartItem.setQuantity(qty);
        cartRepo.save(cartItem);

        response.sendRedirect("/shoppingcart");

        return String.valueOf(subtotal);
    }

    @Transactional
    @RequestMapping(value="/update_cart",params="delete",method=RequestMethod.POST)
    public @ResponseBody void removeProductFromCart(
            @RequestParam Integer productID,
            HttpServletResponse response) throws Exception {

        Users user = activeUser.getInstance().getActiveUser();

        cartServices.removeProduct(productID, user);

        response.sendRedirect("/shoppingcart");
    }


}
