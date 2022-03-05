package myapp.repository;

import myapp.model.CartItem;
import myapp.model.Data_assets;
import myapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    public List<CartItem> findByUser(Users user);

    public CartItem findByUserAndProduct(Users user, Data_assets product);

    @Query("UPDATE CartItem  c SET c.quantity = ?1 WHERE c.product.data_asset_id = ?2 " +
            "AND c.user.id = ?3")
    @Modifying
    public void updateQuantity(Integer quantity, Integer productId, Integer userId);

    @Query("DELETE FROM CartItem c WHERE c.user.id = ?1 AND c.product.data_asset_id = ?2")
    @Modifying
    public void deleteByCustomerAndProduct(Integer userId, Integer productId);
}
