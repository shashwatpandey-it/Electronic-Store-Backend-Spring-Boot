package com.shashwat.electronicstorebackend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequestDto {
	
	@NotBlank(message = "User name required")
	private String userName;
	@NotBlank(message = "Password is required")
	private String password;
}
