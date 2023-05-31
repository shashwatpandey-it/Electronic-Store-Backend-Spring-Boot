package com.shashwat.electronicstorebackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shashwat.electronicstorebackend.jwt.JwtAuthenticationEntryPoint;
import com.shashwat.electronicstorebackend.jwt.JwtAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	private final String[] swaggerUrls = {
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-resources/**",
			"/v2/api-docs",
			"/v3/api-docs",
			"/v3/api-docs/**",
	};
	
	@Bean
	AuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
							.csrf()
							.disable()
							.authorizeHttpRequests()
							.requestMatchers(HttpMethod.POST, "/users", "/auth/**")
							.permitAll()
							.requestMatchers("/test")
							.permitAll()
							.requestMatchers(swaggerUrls)
							.permitAll()
							.anyRequest()
							.authenticated()
							.and()
							.exceptionHandling()
							.authenticationEntryPoint(jwtAuthenticationEntryPoint)
							.and()
							.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and()
							.authenticationProvider(getAuthenticationProvider())
							.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
							.build();
	}
}
