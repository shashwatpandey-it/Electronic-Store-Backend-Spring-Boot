package com.shashwat.electronicstorebackend.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shashwat.electronicstorebackend.dtos.RoleDto;
import com.shashwat.electronicstorebackend.entities.Role;
import com.shashwat.electronicstorebackend.repositories.RoleRepository;
import com.shashwat.electronicstorebackend.services.RoleService;

import jakarta.annotation.PostConstruct;

@Service
public class RoleServiceImpl implements RoleService{

	@Value("${role.id.admin}")
	private String adminId;
	
	@Value("${role.id.normal}")
	private String normalId;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	
	@PostConstruct
	private void createRoles() {

		Role adminRole = Role.builder()
								.roleId(adminId)
								.roleName("ROLE_ADMIN")
								.build();
		
		Role normalRole = Role.builder()
								.roleId(normalId)
								.roleName("ROLE_NORMAL")
								.build();
		
		roleRepository.save(adminRole);
		roleRepository.save(normalRole);
		
		logger.info("----* CONFIGURED ROLES ADDED *----");
	}
	
	@Override
	public RoleDto getNormalRole() {
		Role normalRole = roleRepository.findById(normalId).get();
		return mapper.map(normalRole, RoleDto.class);
	}

	@Override
	public RoleDto getAdminRole() {
		// TODO Auto-generated method stub
		Role adminRole = roleRepository.findById(adminId).get();
		return mapper.map(adminRole, RoleDto.class);
	}
}
