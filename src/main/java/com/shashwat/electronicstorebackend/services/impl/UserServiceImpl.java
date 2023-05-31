package com.shashwat.electronicstorebackend.services.impl;

import java.io.File;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shashwat.electronicstorebackend.dtos.RoleDto;
import com.shashwat.electronicstorebackend.dtos.UserCreationUpdationDto;
import com.shashwat.electronicstorebackend.dtos.UserDto;
import com.shashwat.electronicstorebackend.entities.Role;
import com.shashwat.electronicstorebackend.entities.User;
import com.shashwat.electronicstorebackend.exceptions.ResourceNotFoundException;
import com.shashwat.electronicstorebackend.repositories.UserRepository;
import com.shashwat.electronicstorebackend.services.RoleService;
import com.shashwat.electronicstorebackend.services.UserService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

@Service
public class UserServiceImpl implements UserService {
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private RoleService roleService;

	@Override
	public UserDto createUser(UserCreationUpdationDto userCreationUpdationDto) {
		// TODO Auto-generated method stub
		String id = UUID.randomUUID().toString();
		userCreationUpdationDto.setId(id);
		userCreationUpdationDto.setPassword(passwordEncoder.encode(userCreationUpdationDto.getPassword()));
		RoleDto normalRole = roleService.getNormalRole();
		userCreationUpdationDto.getRoles().add(normalRole);
		User savedUser = userRepository.save(mapper.map(userCreationUpdationDto, User.class));
		return mapper.map(savedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String id) throws IOException {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		if((user.getImageName().equalsIgnoreCase("male-user.jpg")) || (user.getImageName().equalsIgnoreCase("female-user.png"))) {
			user.getRoles().clear();
			userRepository.save(user);
			userRepository.deleteById(id);
		}
		else {
			Path path = Paths.get(imagePath+user.getImageName());
			if(Files.exists(path)) Files.delete(path);
			user.getRoles().clear();
			userRepository.save(user);
			userRepository.deleteById(id);
		}
	}

	@Override
	public UserDto updateUser(UserCreationUpdationDto userCreationUpdationDto, String id) {
		// TODO Auto-generated method stub
		User oldUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		oldUser.setName(userCreationUpdationDto.getName());
		oldUser.setSex(userCreationUpdationDto.getSex());
		oldUser.setAbout(userCreationUpdationDto.getAbout());
		oldUser.setEmail(userCreationUpdationDto.getEmail());
		oldUser.setPassword(passwordEncoder.encode(userCreationUpdationDto.getPassword()));
		
		User updatedUser = userRepository.save(oldUser);
		return mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepository.findAll(pageable);
		PageableResponse<UserDto> pageableResponse = PageableResponse.getPageableResponse(UserDto.class, page);
		return pageableResponse;
	}

	@Override
	public UserDto getUserById(String id) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public PageableResponse<UserDto> searchByName(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepository.findByNameContaining(keyword, pageable);
		PageableResponse<UserDto> pageableResponse = PageableResponse.getPageableResponse(UserDto.class, page);
		return pageableResponse;
	}

	@Override
	public UserDto assignRoleAdmin(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		RoleDto adminRole = roleService.getAdminRole();
		user.getRoles().add(mapper.map(adminRole, Role.class));
		User updatedUser = userRepository.save(user);
		return mapper.map(updatedUser, UserDto.class);
	}
	
}
