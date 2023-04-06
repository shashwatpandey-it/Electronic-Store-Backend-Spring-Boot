package com.shashwat.electronicstorebackend.services;

import java.io.IOException;

import com.shashwat.electronicstorebackend.dtos.CategoryDto;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

public interface CategoryService {

	// create category
	CategoryDto createCategory(CategoryDto categoryDto);
	
	// update category
	CategoryDto updateCategory(CategoryDto categoryDto, String id);
	
	// delete category 
	void deleteCategory(String id) throws IOException;
	
	// get all categories
	PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	// get single category by id
	CategoryDto getCategoryById(String id);
	
	// search categories
	PageableResponse<CategoryDto> searchCategories(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword);
}
