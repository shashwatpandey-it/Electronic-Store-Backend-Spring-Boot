package com.shashwat.electronicstorebackend.dtos;

import java.util.HashSet;
import java.util.Set;

import com.shashwat.electronicstorebackend.utilities.ValidImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationUpdationDto {

	private String id;
	@Size(min = 3 , message = "Not a valid name")
	private String name;
	private String sex;
	private String about;
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email address")
	private String email;
	@NotBlank(message = "Password is required")
	private String password;
	@ValidImage(message = "Not a valid image name")
	private String imageName;
	private Set<RoleDto> roles = new HashSet<>();
}
