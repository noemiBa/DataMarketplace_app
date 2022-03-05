package myapp.services;

import myapp.model.CartItem;
import myapp.model.Users;
import myapp.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class ShoppingCartServices {

    @Autowired
    private CartItemRepository cartRepository;

    public List<CartItem> listCartItems(Users user) {
        return cartRepository.findByUser(user);
    }

    public Double getTotalPrice(Users user) {
        List<CartItem> userItems = cartRepository.findByUser(user);
        Double totalPrice = userItems.stream().mapToDouble(item -> item.getItemCostByQuantity()).sum();

        int temp = (int)(totalPrice*100.0); //get Double to two decimal places
        totalPrice = ((double)temp)/100.0;
        return totalPrice;
    }

    public Integer getTotalQuantity(Users user) {
        List<CartItem> userItems = cartRepository.findByUser(user);
        Integer totalQuantity = userItems.stream().mapToInt(item -> item.getQuantity()).sum();
        return totalQuantity;
    }
}
