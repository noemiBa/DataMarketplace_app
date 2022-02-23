package myapp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    @Query("select u from Users u where u.username=?1")
    Users findByUsername(String username);
}
