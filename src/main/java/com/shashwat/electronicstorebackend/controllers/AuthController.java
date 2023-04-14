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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDto> authenicate(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto){
		AuthenticationResponseDto response = authenticationService.authenticate(authenticationRequestDto);
		LOGGER.info("----* USER AUTHENTICATED *----");
		return new ResponseEntity<AuthenticationResponseDto>(response, HttpStatus.OK);
	}
}
