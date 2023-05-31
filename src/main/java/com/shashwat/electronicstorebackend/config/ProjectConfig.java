package com.shashwat.electronicstorebackend.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ProjectConfig {

	@Bean
	ModelMapper getMapper() {
		return new ModelMapper();
	}
	
	@Bean 
	MappingJackson2HttpMessageConverter octetStreamJsonConvertor() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(List.of(new MediaType("application", "octet-stream")));
		return converter;
	}
}
