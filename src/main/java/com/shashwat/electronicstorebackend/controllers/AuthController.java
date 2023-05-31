package com.shashwat.electronicstorebackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shashwat.electronicstorebackend.dtos.AuthenticationRequestDto;
import com.shashwat.electronicstorebackend.dtos.AuthenticationResponseDto;
import com.shashwat.electronicstorebackend.services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@SecurityRequirements
@Tag(name = "Authnetication Module", description = "This module provides with login service for the registered users")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	@Operation(
				summary = "User login",
				description = "Provides user login with credentials to obtian JWT for authentication to access all other secured endpoints."
			)
	public ResponseEntity<AuthenticationResponseDto> authenicate(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto){
		AuthenticationResponseDto response = authenticationService.authenticate(authenticationRequestDto);
		LOGGER.info("----* USER AUTHENTICATED *----");
		return new ResponseEntity<AuthenticationResponseDto>(response, HttpStatus.OK);
	}
}
