package myapp.repository;

import myapp.model.Order_items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Order_itemsRepository extends JpaRepository<Order_items, Integer> {}