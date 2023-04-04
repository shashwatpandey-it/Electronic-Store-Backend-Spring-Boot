package com.shashwat.electronicstorebackend.controllers;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shashwat.electronicstorebackend.dtos.UserCreationUpdationDto;
import com.shashwat.electronicstorebackend.dtos.UserDto;
import com.shashwat.electronicstorebackend.services.UserImageService;
import com.shashwat.electronicstorebackend.services.UserService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UserImageService userImageService;

	@PostMapping
	public ResponseEntity<UserDto> createUserEntity(@Valid @RequestBody UserCreationUpdationDto userCreationUpdationDto) throws IOException{
		String imageName = userImageService.setDefaultImage(userCreationUpdationDto.getSex());
		userCreationUpdationDto.setImageName(imageName);
		UserDto userDto = userService.createUser(userCreationUpdationDto);
		LOGGER.info("----* USER CREATED *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
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
	public ResponseEntity<UserDto> updateUserEntity(@Valid @RequestBody UserCreationUpdationDto userCreationUpdationDto, @PathVariable ("id") String id){
		UserDto userDto = userService.updateUser(userCreationUpdationDto, id);
		LOGGER.info("----* USER UPDATED *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@GetMapping
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
	public ResponseEntity<UserDto> fetchUserEntityById(@PathVariable ("id") String id){
		UserDto userDto = userService.getUserById(id);
		LOGGER.info("----* USER FETCHED BY ID *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> fetchUserEntityByEmail(@PathVariable ("email") String email){
		UserDto userDto = userService.getUserByEmail(email);
		LOGGER.info("----* USER FETCHED BY EMAIL *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/search/{keyword}")
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
	public ResponseEntity<ResponseMessage> uploadUserImage(
			@PathVariable("id") String id,
			@RequestParam MultipartFile file) throws IOException
	{
		String imageName = userImageService.uploadImage(file, imageUploadPath, id);
		ResponseMessage message = ResponseMessage.builder()
													.message("image uploaded with name : " + imageName)
													.actionPerformed(true)
													.build();
		LOGGER.info("----* USER IMAGE UPLOADED *----");
	    return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@GetMapping("/image/{id}")
	public void serveUserImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		UserDto userDto = userService.getUserById(id);
		InputStream inputStream = userImageService.getImageResource(imageUploadPath, userDto.getImageName());
		LOGGER.info("----* USER IMAGE SERVED *----");
		StreamUtils.copy(inputStream, response.getOutputStream());
	}
}
