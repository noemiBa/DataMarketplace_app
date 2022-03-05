package myapp.repository;

import myapp.model.CartItem;
import myapp.model.Data_assets;
import myapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface Data_assetsRepository extends JpaRepository<Data_assets, Integer> {
	List<Data_assets> findByFeaturedTrue();


}

