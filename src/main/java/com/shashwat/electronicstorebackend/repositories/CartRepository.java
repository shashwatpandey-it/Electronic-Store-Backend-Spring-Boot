package com.shashwat.electronicstorebackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shashwat.electronicstorebackend.entities.Cart;
import com.shashwat.electronicstorebackend.entities.User;

public interface CartRepository extends JpaRepository<Cart, String>{

	Optional<Cart> findByUser(User user);
}
