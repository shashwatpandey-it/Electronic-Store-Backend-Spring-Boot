package com.shashwat.electronicstorebackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shashwat.electronicstorebackend.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

	Page<Category> findByTitleContaining(String keyword, Pageable pageable);
	
	@Modifying 
	@Query("update Category c set c.coverImageName = :coverImageName where c.id = :id")
	int updateCategorySetCoverImageNameForId(@Param("coverImageName") String coverImageName, @Param("id") String id);
}
