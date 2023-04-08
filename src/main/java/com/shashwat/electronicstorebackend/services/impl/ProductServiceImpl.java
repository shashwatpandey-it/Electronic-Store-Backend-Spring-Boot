package com.shashwat.electronicstorebackend.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shashwat.electronicstorebackend.dtos.ProductDto;
import com.shashwat.electronicstorebackend.entities.Category;
import com.shashwat.electronicstorebackend.entities.Product;
import com.shashwat.electronicstorebackend.exceptions.ResourceNotFoundException;
import com.shashwat.electronicstorebackend.repositories.CategoryRepository;
import com.shashwat.electronicstorebackend.repositories.ProductRepository;
import com.shashwat.electronicstorebackend.services.ProductService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Value("${product.image.path}")
	private String imagePath;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public ProductDto createProduct(ProductDto productDto) {
		// TODO Auto-generated method stub
		String id = UUID.randomUUID().toString();
		productDto.setId(id);
		productDto.setListingDate(LocalDate.now());
		Product savedProduct = productRepository.save(mapper.map(productDto, Product.class));
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String id) {
		// TODO Auto-generated method stub
		Product oldProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such product exists"));
		oldProduct.setManufacturer(productDto.getManufacturer());
		oldProduct.setName(productDto.getName());
		oldProduct.setDescription(productDto.getDescription());
		oldProduct.setQuantity(productDto.getQuantity());
		oldProduct.setListedPrice(productDto.getListedPrice());
		oldProduct.setDiscountedPrice(productDto.getDiscountedPrice());
		oldProduct.setListingDate(LocalDate.now());
		oldProduct.setStock(productDto.isStock());
		
		Product updatedProduct =productRepository.save(oldProduct);
		return mapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public void deleteProduct(String id) throws IOException {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such product exists"));
		Path path = Paths.get(imagePath+product.getImageName());
		if(Files.exists(path)) {
			Files.delete(path);
		}
		productRepository.deleteById(id);
	}
	
	@Override
	public ProductDto moveToCategory(String productId, String categoryId) {
		// TODO Auto-generated method stub
		Product oldProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No such product exists"));
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No such category exists"));
		oldProduct.addCategory(category);
		Product updatedProduct = productRepository.save(oldProduct);
		return mapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

	@Override
	public ProductDto getById(String id) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such product exists"));
		return mapper.map(product, ProductDto.class);
	}
	
	@Override
	public PageableResponse<ProductDto> getAllProductsOfCategory(int pageNumber, int pageSize, String sortBy, String sortDir, String categoryId) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No such category exist"));
		Page<Product> page = productRepository.findByCategories(pageable, category);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> searchProductsByManufacturer(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByManufacturerContaining(pageable, keyword);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> searchProductsByName(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByNameContaining(pageable, keyword);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> filterProductsByListedPrice(int pageNumber, int pageSize, String sortBy, String sortDir, float price) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByListedPriceLessThanEqual(pageable, price);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> filterProductsByDiscountedPrice(int pageNumber, int pageSize, String sortBy, String sortDir, float price) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByDiscountedPriceLessThanEqual(pageable, price);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> getProductsInStock(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByStockTrue(pageable);
		PageableResponse<ProductDto> pageableResponse = PageableResponse.getPageableResponse(ProductDto.class, page);
		return pageableResponse;
	}

}
