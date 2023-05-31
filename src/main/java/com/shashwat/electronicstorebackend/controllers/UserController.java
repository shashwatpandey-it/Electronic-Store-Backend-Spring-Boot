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
import org.springframework.web.multipart.MultipartRequest;

import com.shashwat.electronicstorebackend.dtos.UserCreationUpdationDto;
import com.shashwat.electronicstorebackend.dtos.UserDto;
import com.shashwat.electronicstorebackend.services.ImageService;
import com.shashwat.electronicstorebackend.services.UserService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User Module", description = "Endpoints for user management operations")
public class UserController {
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	private static final String ENTITY_USER = "user";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@SecurityRequirements
	@Operation(
				summary = "Create new user",
				description = "Create a new user in the system with option to upload user's profile image at the time of creation."
			)
	public ResponseEntity<UserDto> createUserEntity(
			@Valid @RequestPart("data") UserCreationUpdationDto userCreationUpdationDto,
			@RequestPart(name = "file", required = false) MultipartFile file) throws IOException
	{
		String imageName = imageService.setDefaultImage(userCreationUpdationDto.getSex());
		userCreationUpdationDto.setImageName(imageName);
		UserDto userDto = userService.createUser(userCreationUpdationDto);
		if(!(file == null || file.isEmpty())) {
			String savedImageName = imageService.uploadImage(file, imageUploadPath, userDto.getId(), ENTITY_USER);
			userDto.setImageName(savedImageName);
		}
		LOGGER.info("----* USER CREATED *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	@Operation(
				summary = "Delete user",
				description = "Delete an existing user from the system."
			)
	public ResponseEntity<ResponseMessage> deleteUserEntity(@PathVariable ("id") String id) throws IOException{
		userService.deleteUser(id);
		LOGGER.info("----* USER DELETED *----");
		ResponseMessage message = ResponseMessage.builder()
													.message("user with id " + id + " deleted successfully")
													.actionPerformed(true)
													.build();
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@Operation(
				summary = "Update user",
				description = "Update details of an existing user in the system."
			)
	public ResponseEntity<UserDto> updateUserEntity(@Valid @RequestBody UserCreationUpdationDto userCreationUpdationDto, @PathVariable ("id") String id){
		UserDto userDto = userService.updateUser(userCreationUpdationDto, id);
		LOGGER.info("----* USER UPDATED *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/admin")
	@Operation(
				summary = "Make admin",
				description = "Make an existing user admin. This operation can only be performed by a user that has a role - admin."
			)
	public ResponseEntity<UserDto> makeAdmin(@PathVariable("id") String id){
		UserDto userDto = userService.assignRoleAdmin(id);
		LOGGER.info("----* USER IS NOW AN ADMIN *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@GetMapping
	@Operation(
				summary = "Get all users",
				description = "Fetch list of all users in the system.(Pagination and sorting implemented)"
			)
	public ResponseEntity<PageableResponse<UserDto>> fetchAllUserEntity(
			@RequestParam (value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)
	{
		PageableResponse<UserDto> list = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
		LOGGER.info("----* ALL USERS FETCHED *----");
		return new ResponseEntity<PageableResponse<UserDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Operation(
				summary = "Get user by id",
				description = "Fetch a particular user with his/her id."
			)
	public ResponseEntity<UserDto> fetchUserEntityById(@PathVariable ("id") String id){
		UserDto userDto = userService.getUserById(id);
		LOGGER.info("----* USER FETCHED BY ID *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/email/{email}")
	@Operation(
				summary = "Get user by email",
				description = "Fetch a particular user with his/her email."
			)	
	public ResponseEntity<UserDto> fetchUserEntityByEmail(@PathVariable ("email") String email){
		UserDto userDto = userService.getUserByEmail(email);
		LOGGER.info("----* USER FETCHED BY EMAIL *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/search/{keyword}")
	@Operation(
				summary = "Search users",
				description = "Search users by name.(Pagination and sorting implemented)"
			)
	public ResponseEntity<PageableResponse<UserDto>> searchUserEntity(
			@PathVariable ("keyword") String keyword,
			@RequestParam (value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam (value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)
	{
		PageableResponse<UserDto> list = userService.searchByName(keyword, pageNumber, pageSize, sortBy, sortDir);
		LOGGER.info("----* USER FOUND *----");
		return new ResponseEntity<PageableResponse<UserDto>>(list, HttpStatus.FOUND);
	}
	
	@PostMapping("/image-upload/{id}")
	@Operation(
				summary = "Upload user image",
				description = "Upload profile image of an existing user."
			)
	public ResponseEntity<ResponseMessage> uploadUserImage(
			@PathVariable("id") String id,
			@RequestParam MultipartFile file) throws IOException
	{
		String imageName = imageService.uploadImage(file, imageUploadPath, id, ENTITY_USER);
		ResponseMessage message = ResponseMessage.builder()
													.message("image uploaded with name : " + imageName)
													.actionPerformed(true)
													.build();
		LOGGER.info("----* USER IMAGE UPLOADED *----");
	    return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@GetMapping("/image/{id}")
	@Operation(
				summary = "View user image",
				description = "View image of a particular user."
			)
	public void serveUserImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		UserDto userDto = userService.getUserById(id);
		InputStream inputStream = imageService.getImageResource(imageUploadPath, userDto.getImageName());
		LOGGER.info("----* USER IMAGE SERVED *----");
		StreamUtils.copy(inputStream, response.getOutputStream());
	}
}
