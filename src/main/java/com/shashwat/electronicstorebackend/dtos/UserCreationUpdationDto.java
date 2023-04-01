package com.shashwat.electronicstorebackend.dtos;

import java.util.List;

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
	private String name;
	private String sex;
	private String about;
	private String email;
	private String password;
	private String imageName;
}
