package com.shashwat.electronicstorebackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

	@Bean
	ModelMapper getMapper() {
		return new ModelMapper();
	}
}
