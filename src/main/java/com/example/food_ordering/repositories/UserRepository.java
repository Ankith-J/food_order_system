package com.example.food_ordering.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.food_ordering.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
