package com.shashwat.electronicstorebackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashwat.electronicstorebackend.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);
	List<User> findByNameContaining(String keyword);

}
