package com.shashwat.electronicstorebackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shashwat.electronicstorebackend.entities.Category;
import com.shashwat.electronicstorebackend.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	Page<Product> findByManufacturerContaining(Pageable pageable, String keyword);
	Page<Product> findByNameContaining(Pageable pageable, String keyword);
	Page<Product> findByStockTrue(Pageable pageable);
	Page<Product> findByDiscountedPriceLessThanEqual(Pageable pageable, float price);
	Page<Product> findByListedPriceLessThanEqual(Pageable pageable, float price);
	Page<Product> findByCategories(Pageable pageable, Category category);
	
	@Modifying
	@Query("update Product p set p.imageName = :imageName where p.id = :id")
	int updateProductSetImageNameForId(@Param("imageName") String imageName, @Param("id") String id);
	
	@Modifying
	@Query("update Product p set p.quantity = :remainingQty where p.id = :id")
	int updateProductSetQuantityForId(@Param("remainingQty") int remainingQty, @Param("id") String id);
}
