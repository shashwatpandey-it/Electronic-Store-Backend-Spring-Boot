package com.shashwat.electronicstorebackend.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.shashwat.electronicstorebackend.dtos.ProductDto;
import com.shashwat.electronicstorebackend.entities.Category;
import com.shashwat.electronicstorebackend.services.CategoryService;
import com.shashwat.electronicstorebackend.services.ImageService;
import com.shashwat.electronicstorebackend.services.ProductService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private static final String ENTITY_PRODUCT = "product";
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	@Value("${product.image.path}")
	private String imageUploadPath;
	@Autowired
	private ProductService productService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ProductDto> createProductEntity(
			@Valid @RequestPart("data") ProductDto productDto,
			@RequestPart("file") MultipartFile file) throws IOException
	{
		ProductDto productDto2 = productService.createProduct(productDto);
		if(!file.isEmpty()) {
			String imageName = imageService.uploadImage(file, imageUploadPath, productDto2.getId(), ENTITY_PRODUCT);
			productDto2.setImageName(imageName);
		}
		LOGGER.info("----* PRODUCT CREATED *----");
		return new ResponseEntity<ProductDto>(productDto2, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/in-category/{categoryId}",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ProductDto> createProductInCategory(
			@PathVariable("categoryId") String categoryId,
			@Valid @RequestPart("data") ProductDto productDto,
			@RequestPart("file") MultipartFile file) throws IOException
	{
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		productDto.addCategory(categoryDto);
		ProductDto productDto2 = productService.createProduct(productDto);
		if(!file.isEmpty()) {
			String imageName = imageService.uploadImage(file, imageUploadPath, productDto2.getId(), ENTITY_PRODUCT);
			productDto2.setImageName(imageName);
		}
		LOGGER.info("----* PRODUCT CREATED IN CATEGORY *----");
		return new ResponseEntity<ProductDto>(productDto2, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> updateProductEntity(
			@Valid @RequestBody ProductDto productDto,
			@PathVariable ("id") String id)
	{
		ProductDto productDto2 = productService.updateProduct(productDto, id);
		LOGGER.info("----* PRODUCT UPDATED *----");
		return new ResponseEntity<ProductDto>(productDto2, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseMessage> deleteProductEntity(@PathVariable("id") String id) throws IOException{
		productService.deleteProduct(id);
		LOGGER.info("---- * PRODUCT DELETED *----");
		ResponseMessage message = ResponseMessage.builder()
													.message("Product with id : " + id + " deleted")
													.actionPerformed(true)
													.build();
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@PutMapping("{productId}/to-category/{categoryId}")
	public ResponseEntity<ProductDto> moveProductToCategory(
			@PathVariable("productId") String productId,
			@PathVariable("categoryId") String categoryId)
	{
		ProductDto productDto = productService.moveToCategory(productId, categoryId);
		LOGGER.info("----* PRODUCT ADDED TO CATEGORY *----");
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK); 
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> fetchAllProducts(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir)
	{
		PageableResponse<ProductDto> list = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
		LOGGER.info("---- * ALL PRODUCTS FETCHED *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> fetchProductById(@PathVariable("id") String id){
		ProductDto productDto = productService.getById(id);
		LOGGER.info("---- * PRODUCT FETCHED BY ID *----");
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.FOUND);
	}
	
	@GetMapping("in-category/{categoryId}")
	public ResponseEntity<PageableResponse<ProductDto>> productsInCategory(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable("categoryId") String categoryId)
	{
		PageableResponse<ProductDto> list = productService.getAllProductsOfCategory(pageNumber, pageSize, sortBy, sortDir, categoryId);
		LOGGER.info("---- * ALL PRODUCTS OF CATEGORY FETCHED *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/instock")
	public ResponseEntity<PageableResponse<ProductDto>> productsInStock(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir)
	{
		PageableResponse<ProductDto> list = productService.getProductsInStock(pageNumber, pageSize, sortBy, sortDir);
		LOGGER.info("---- * ALL PRODUCTS IN STOCK FETCHED *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.OK);		
	}
	
	@GetMapping("/search/brand/{keyword}")
	public ResponseEntity<PageableResponse<ProductDto>> searchByManufacturer(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable ("keyword") String keyword)
	{
		PageableResponse<ProductDto> list = productService.searchProductsByManufacturer(pageNumber, pageSize, sortBy, sortDir, keyword);
		LOGGER.info("----* SEARCHED RESULTS *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.FOUND);	
	}
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<PageableResponse<ProductDto>> searchByName(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable ("keyword") String keyword)
	{
		PageableResponse<ProductDto> list = productService.searchProductsByName(pageNumber, pageSize, sortBy, sortDir, keyword);
		LOGGER.info("----* SEARCHED RESULTS *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.FOUND);	
	}
	
	@GetMapping("/filter/list-price/{price}")
	public ResponseEntity<PageableResponse<ProductDto>> filterByListPrice(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "listedPrice", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable ("price") float price)
	{
		PageableResponse<ProductDto> list = productService.filterProductsByListedPrice(pageNumber, pageSize, sortBy, sortDir, price);
		LOGGER.info("----* SEARCHED RESULTS *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.FOUND);	
	}
	
	@GetMapping("/filter/discounted-price/{price}")
	public ResponseEntity<PageableResponse<ProductDto>> filterByDiscountedPrice(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "discountedPrice", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable ("price") float price)
	{
		PageableResponse<ProductDto> list = productService.filterProductsByDiscountedPrice(pageNumber, pageSize, sortBy, sortDir, price);
		LOGGER.info("----* SEARCHED RESULTS *----");
		return new ResponseEntity<PageableResponse<ProductDto>>(list, HttpStatus.FOUND);	
	}
	
	@PostMapping("/image-upload/{id}")
	public ResponseEntity<ResponseMessage> uploadProductImage(
			@PathVariable("id") String id,
			@RequestParam MultipartFile file) throws IOException
	{
		String imageName = imageService.uploadImage(file, imageUploadPath, id, ENTITY_PRODUCT);
		ResponseMessage message = ResponseMessage.builder()
													.message("image uploaded with name : " + imageName)
													.actionPerformed(true)
													.build();
		LOGGER.info("----* PRODUCT IMAGE UPLOADED *----");
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@GetMapping("/image/{id}")
	public void serveCategoryImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		ProductDto productDto = productService.getById(id);
		InputStream inputStream = imageService.getImageResource(imageUploadPath, productDto.getImageName());
		LOGGER.info("----* PRODUCT IMAGE SERVED *----");
		StreamUtils.copy(inputStream, response.getOutputStream());
	}
}
