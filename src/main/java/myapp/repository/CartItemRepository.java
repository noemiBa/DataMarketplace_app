package myapp.repository;

import myapp.model.CartItem;
import myapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    public List<CartItem> findByUser(Users user);
}
