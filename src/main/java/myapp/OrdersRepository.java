package myapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
