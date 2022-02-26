package myapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface Order_itemsRepository extends JpaRepository<Order_items, Integer> {}