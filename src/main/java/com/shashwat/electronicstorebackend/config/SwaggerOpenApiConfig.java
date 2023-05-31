package com.shashwat.electronicstorebackend.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
			info = @Info(
						contact = @Contact(
								name = "Shashwat Pandey (Github)",
								email = "2001shashwat.rdr@gmail.com",
								url = "https://github.com/shashwat9470"
							),
						title = "Electronic Store Backend - Api",
						description = "This api is for the backend application (built using Spring Boot) of an electronic store.",
						version = "1.0"
					),
			security = @SecurityRequirement(name = "JWT Authentication")
		)
@SecurityScheme(
			name = "JWT Authentication",
			description = "Place here the JWT token obtained after login.",
			type = SecuritySchemeType.HTTP,
			scheme = "bearer",
			bearerFormat = "JWT",
			in = SecuritySchemeIn.HEADER
		)
@Configuration
public class SwaggerOpenApiConfig {
	
	public SwaggerOpenApiConfig(MappingJackson2HttpMessageConverter converter) {
		var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "octet-stream"));
		converter.setSupportedMediaTypes(supportedMediaTypes);
	}

}
