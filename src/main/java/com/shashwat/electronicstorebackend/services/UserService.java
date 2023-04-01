package com.shashwat.electronicstorebackend.services;

import java.util.List;

import com.shashwat.electronicstorebackend.dtos.UserCreationUpdationDto;
import com.shashwat.electronicstorebackend.dtos.UserDto;

public interface UserService {

	// create user
	UserDto createUser(UserCreationUpdationDto userCreationUpdationDto);
	
	// delete user
	void deleteUser(String id);
	
	// update user
	UserDto updateUser(UserCreationUpdationDto userCreationUpdationDto, String id);
	
	// get all users
	List<UserDto> getAllUsers();
	
	// get user by id
	UserDto getUserById(String id);
	
	// get user by email
	UserDto getUserByEmail(String email);
	
	// search user by name
	List<UserDto> searchByName(String keyword);
}
