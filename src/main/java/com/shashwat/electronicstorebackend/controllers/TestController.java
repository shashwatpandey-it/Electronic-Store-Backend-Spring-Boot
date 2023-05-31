package com.shashwat.electronicstorebackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Test Endpoint", description = "Hit the endpoint to confirm working of api")
public class TestController {
	
	private Logger logger = LoggerFactory.getLogger(TestController.class);

	@GetMapping("/test")
	@SecurityRequirements
	@Operation(
				summary = "Testing endpoint",
				description = "Hit this endpoint to obtain a string"+" (testing successful)"+" in response to verify that system is up and running."
			)
	public ResponseEntity<String> test(){
		logger.info("from the controller");
		return new ResponseEntity<String>("testing successful", HttpStatus.OK);
	}
}
