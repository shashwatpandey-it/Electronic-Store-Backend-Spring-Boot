package com.shashwat.electronicstorebackend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shashwat.electronicstorebackend.dtos.UserCreationUpdationDto;
import com.shashwat.electronicstorebackend.dtos.UserDto;
import com.shashwat.electronicstorebackend.services.impl.UserServiceImpl;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserServiceImpl userServiceImpl;

	@PostMapping
	public ResponseEntity<UserDto> createUserEntity(@RequestBody UserCreationUpdationDto userCreationUpdationDto){
		UserDto userDto = userServiceImpl.createUser(userCreationUpdationDto);
		LOGGER.info("----* USER CREATED *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseMessage> deleteUserEntity(@PathVariable ("id") String id){
		userServiceImpl.deleteUser(id);
		LOGGER.info("----* USER DELETED *----");
		ResponseMessage message = ResponseMessage.builder()
													.message("user with id" + id + " deleted successfully")
													.actionPerformed(true)
													.build();
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUserEntity(@RequestBody UserCreationUpdationDto userCreationUpdationDto, @PathVariable ("id") String id){
		UserDto userDto = userServiceImpl.updateUser(userCreationUpdationDto, id);
		LOGGER.info("----* USER UPDATED *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> fetchAllUserEntity(){
		List<UserDto> list = userServiceImpl.getAllUsers();
		LOGGER.info("----* ALL USERS FETCHED *----");
		return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> fetchUserEntityById(@PathVariable ("id") String id){
		UserDto userDto = userServiceImpl.getUserById(id);
		LOGGER.info("----* USER FETCHED BY ID *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> fetchUserEntityByEmail(@PathVariable ("email") String email){
		UserDto userDto = userServiceImpl.getUserByEmail(email);
		LOGGER.info("----* USER FETCHED BY EMAIL *----");
		return new ResponseEntity<UserDto>(userDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> searchUserEntity(@PathVariable ("keyword") String keyword){
		List<UserDto> list = userServiceImpl.searchByName(keyword);
		LOGGER.info("----* USER FOUND *----");
		return new ResponseEntity<List<UserDto>>(list, HttpStatus.FOUND);
	}
	
}
