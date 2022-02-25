package myapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface Data_assetsRepository extends JpaRepository<Data_assets, Integer> {
	List<Data_assets> findByFeaturedTrue();
}
