package com.shashwat.electronicstorebackend.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shashwat.electronicstorebackend.dtos.CategoryDto;
import com.shashwat.electronicstorebackend.entities.Category;
import com.shashwat.electronicstorebackend.exceptions.ResourceNotFoundException;
import com.shashwat.electronicstorebackend.repositories.CategoryRepository;
import com.shashwat.electronicstorebackend.services.CategoryService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Value("${category.cover.image.path}")
	private String imagePath;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		// TODO Auto-generated method stub
		String id = UUID.randomUUID().toString();
		categoryDto.setId(id);
		Category savedCategory = categoryRepository.save(mapper.map(categoryDto, Category.class));
		return mapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, String id) {
		// TODO Auto-generated method stub
		Category oldCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such category exits"));
		oldCategory.setTitle(categoryDto.getTitle());
		Category updatedCategory = categoryRepository.save(oldCategory);
		return mapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(String id) throws IOException {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such category exits"));
		Path path = Paths.get(imagePath+category.getCoverImageName());
		if(Files.exists(path)) {
			Files.delete(path);
		}
		categoryRepository.deleteById(id);	
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> page = categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> pageableResponse = PageableResponse.getPageableResponse(CategoryDto.class, page);
		return pageableResponse;
	}

	@Override
	public CategoryDto getCategoryById(String id) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such category exits"));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public PageableResponse<CategoryDto> searchCategories(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> page = categoryRepository.findByTitleContaining(keyword, pageable);
		PageableResponse<CategoryDto> pageableResponse = PageableResponse.getPageableResponse(CategoryDto.class, page);
 		return pageableResponse;
	}

}
