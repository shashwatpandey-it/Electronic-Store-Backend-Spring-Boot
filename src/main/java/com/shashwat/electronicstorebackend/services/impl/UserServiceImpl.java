package com.shashwat.electronicstorebackend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shashwat.electronicstorebackend.dtos.UserCreationUpdationDto;
import com.shashwat.electronicstorebackend.dtos.UserDto;
import com.shashwat.electronicstorebackend.entities.User;
import com.shashwat.electronicstorebackend.repositories.UserRepository;
import com.shashwat.electronicstorebackend.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public UserDto createUser(UserCreationUpdationDto userCreationUpdationDto) {
		// TODO Auto-generated method stub
		String id = UUID.randomUUID().toString();
		userCreationUpdationDto.setId(id);
		User savedUser = userRepository.save(mapper.map(userCreationUpdationDto, User.class));
		return mapper.map(savedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
	}

	@Override
	public UserDto updateUser(UserCreationUpdationDto userCreationUpdationDto, String id) {
		// TODO Auto-generated method stub
		User oldUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
		oldUser.setName(userCreationUpdationDto.getName());
		oldUser.setSex(userCreationUpdationDto.getSex());
		oldUser.setAbout(userCreationUpdationDto.getAbout());
		oldUser.setEmail(userCreationUpdationDto.getEmail());
		oldUser.setPassword(userCreationUpdationDto.getPassword());
		oldUser.setImageName(userCreationUpdationDto.getImageName());
		
		User updatedUser = userRepository.save(oldUser);
		return mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> usersList = userRepository.findAll();
		return usersList.stream().map((user) -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public UserDto getUserById(String id) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> searchByName(String keyword) {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findByNameContaining(keyword);
		return users.stream().map((user) -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
	}
	
}
