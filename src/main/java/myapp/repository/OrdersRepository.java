package myapp.repository;

import myapp.model.Orders;
import myapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    @Query("UPDATE Orders  o SET o.status = ?1 WHERE o.order_id = ?2 " +
            "AND o.user.id = ?3")
    @Modifying
    public void updateStatus(String newStatus, Integer order_id, Integer userId);

    public List<Orders> findByUser(Users user);
}
