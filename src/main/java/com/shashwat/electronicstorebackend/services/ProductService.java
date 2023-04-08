package com.shashwat.electronicstorebackend.services;

import java.io.IOException;

import com.shashwat.electronicstorebackend.dtos.ProductDto;
import com.shashwat.electronicstorebackend.entities.Category;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

public interface ProductService {

	// save product
	ProductDto createProduct(ProductDto productDto);
	
	// update product
	ProductDto updateProduct(ProductDto productDto, String id);
	
	// delete product
	void deleteProduct(String id) throws IOException;
	
	// move product to category
	ProductDto moveToCategory(String productId, String categoryId);
	
	// get all products
	PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	// get product by id
	ProductDto getById(String id);
	
	// get all products in stock
	PageableResponse<ProductDto> getProductsInStock(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	// get all products of given category
	PageableResponse<ProductDto> getAllProductsOfCategory(int pageNumber, int pageSize, String sortBy, String sortDir, String categoryId);
	
	// search products by manufacturer
	PageableResponse<ProductDto> searchProductsByManufacturer(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword);
	
	// search products by name
	PageableResponse<ProductDto> searchProductsByName(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword);
	
	// filter products by listed price 
	PageableResponse<ProductDto> filterProductsByListedPrice(int pageNumber, int pageSize, String sortBy, String sortDir, float price);
	
	// filter products by discounted price
	PageableResponse<ProductDto> filterProductsByDiscountedPrice(int pageNumber, int pageSize, String sortBy, String sortDir, float price);
	
}
