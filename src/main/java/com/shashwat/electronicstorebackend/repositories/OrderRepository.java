package com.shashwat.electronicstorebackend.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shashwat.electronicstorebackend.entities.Order;
import com.shashwat.electronicstorebackend.entities.User;

public interface OrderRepository extends JpaRepository<Order, String>{
	
	Page<Order> findAllByUser(Pageable pageable, User user);

}
