package com.shashwat.electronicstorebackend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shashwat.electronicstorebackend.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);
	Page<User> findByNameContaining(String keyword, Pageable pageable);
	
	@Modifying
	@Query("update User u set u.imageName = :imageName where u.id = :id")
	int updateUserSetImageNameForId(@Param("imageName") String imageName, @Param("id") String id);

}
