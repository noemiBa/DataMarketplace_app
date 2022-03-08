package myapp.repository;

import myapp.model.Order_items;
import myapp.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Order_itemsRepository extends JpaRepository<Order_items, Integer> {

    @Query("select i from Order_items i where i.order=?1")
    public List<Order_items> listOrderItemsByOrder(Orders order);
}