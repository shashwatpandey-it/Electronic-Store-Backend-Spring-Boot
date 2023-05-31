package com.shashwat.electronicstorebackend.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shashwat.electronicstorebackend.dtos.CategoryDto;
import com.shashwat.electronicstorebackend.services.CategoryService;
import com.shashwat.electronicstorebackend.services.ImageService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category Module", description = "Endpoints for CRUD operations, fetching and searching operation for product categories")
public class CategoryController {
	
	private static final String ENTITY_CATEGORY = "category";
	
	@Value("${category.cover.image.path}")
	private String imageUploadPath;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ImageService imageService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
			summary = "Create new category",
			description = "Create a new category in the system with option to upload category image at the time of creation. This operation can only be performed by admin."
		)
	public ResponseEntity<CategoryDto> createCategoryEntity(
			@Valid @RequestPart("data") CategoryDto categoryDto,
			@RequestPart("file") MultipartFile file) throws IOException
	{
		CategoryDto categoryDto2 = categoryService.createCategory(categoryDto);
		if(!(file == null || file.isEmpty())) {
			String savedImageName = imageService.uploadImage(file, imageUploadPath, categoryDto2.getId(), ENTITY_CATEGORY);
			categoryDto2.setCoverImageName(savedImageName);
		}
		LOGGER.info("----* CATEGORY CREATED *----");
		return new ResponseEntity<CategoryDto>(categoryDto2, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	@Operation(
			summary = "Update category",
			description = "Update details of an existing category in the system. This operation can only be performed by admin."
			)
	public ResponseEntity<CategoryDto> updateCategoryEntity(
			@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable ("id") String id)
	{
		CategoryDto categoryDto2 = categoryService.updateCategory(categoryDto, id);
		LOGGER.info("----* CATEGORY UPDATED *----");
		return new ResponseEntity<CategoryDto>(categoryDto2, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	@Operation(
			summary = "Delete category",
			description = "Delete an existing category from the system. This operation can only be performed by admin."
		)
	public ResponseEntity<ResponseMessage> deleteCategoryEntity(@PathVariable ("id") String id) throws IOException{
		categoryService.deleteCategory(id);
		ResponseMessage message = ResponseMessage.builder()
													.message("Category with id : " + id + " deleted")
													.actionPerformed(true)
													.build();
		LOGGER.info("----* CATEGORY DELETED *----");
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@GetMapping
	@Operation(
			summary = "Get all categories",
			description = "Fetch list of all categories in the system.(Pagination and sorting implemented)"
		)
	public ResponseEntity<PageableResponse<CategoryDto>> fetchAllCAtegories(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir)
	{
		PageableResponse<CategoryDto> list = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
		LOGGER.info("----* ALL CATEGORIES FETCHED *----");
		return new ResponseEntity<PageableResponse<CategoryDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Operation(
			summary = "Get category by id",
			description = "Fetch a particular category with it's id."
		)
	public ResponseEntity<CategoryDto> fetchCategoryById(@PathVariable ("id") String id){
		CategoryDto categoryDto = categoryService.getCategoryById(id);
		LOGGER.info("----* CATEGORY FETCHED BY ID *----");
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/search/{keyword}")
	@Operation(
			summary = "Search category by name",
			description = "Search category by name.(Pagination and sorting implemented)"
		)
	public ResponseEntity<PageableResponse<CategoryDto>> searchCategory(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable ("keyword") String keyword)
	{
		PageableResponse<CategoryDto> list = categoryService.searchCategories(pageNumber, pageSize, sortBy, sortDir, keyword);
		LOGGER.info("----* SEARCHED RESULTS *----");
		return new ResponseEntity<PageableResponse<CategoryDto>>(list, HttpStatus.FOUND);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image-upload/{id}")
	@Operation(
			summary = "Upload category image",
			description = "Upload image of an existing category. This operation can only be performed by admin."
		)
	public ResponseEntity<ResponseMessage> uploadCategoryImage(
			@PathVariable("id") String id,
			@RequestParam MultipartFile file) throws IOException
	{
		String imageName = imageService.uploadImage(file, imageUploadPath, id, ENTITY_CATEGORY);
		ResponseMessage message = ResponseMessage.builder()
													.message("image uploaded with name : " + imageName)
													.actionPerformed(true)
													.build();
		LOGGER.info("----* CATEGORY IMAGE UPLOADED *----");
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@GetMapping("/image/{id}")
	@Operation(
			summary = "View category image",
			description = "View image of a particular category."
		)
	public void serveCategoryImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		CategoryDto categoryDto = categoryService.getCategoryById(id);
		InputStream inputStream = imageService.getImageResource(imageUploadPath, categoryDto.getCoverImageName());
		LOGGER.info("----* CATEGORY IMAGE SERVED *----");
		StreamUtils.copy(inputStream, response.getOutputStream());
	}
	
}
