package myapp.services;

import myapp.model.CartItem;
import myapp.model.Users;
import myapp.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServices {

    @Autowired
    private CartItemRepository cartRepository;

    public List<CartItem> listCartItems(Users user) {
        return cartRepository.findByUser(user);
    }
}
