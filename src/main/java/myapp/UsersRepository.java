package myapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("select u from Users u where u.username=?1")
    Users findByUsername(String username);
}
