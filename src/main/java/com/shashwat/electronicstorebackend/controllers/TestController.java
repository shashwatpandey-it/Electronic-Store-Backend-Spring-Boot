package com.shashwat.electronicstorebackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
	
	private Logger logger = LoggerFactory.getLogger(TestController.class);

	@GetMapping("/test")
	public ResponseEntity<String> test(){
		logger.info("from the controller");
		return new ResponseEntity<String>("testing successful", HttpStatus.OK);
	}
}
