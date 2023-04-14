package com.shashwat.electronicstorebackend.dtos;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class UserDto {
	
	private String id;
	private String name;
	private String sex;
	private String about;
	private String email;
	private String imageName;
	private Set<RoleDto> roles = new HashSet<>();

}
