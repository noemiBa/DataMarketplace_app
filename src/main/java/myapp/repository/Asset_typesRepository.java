package myapp.repository;

import myapp.model.Asset_types;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

public interface Asset_typesRepository extends JpaRepository<Asset_types, Integer> {
	
}
