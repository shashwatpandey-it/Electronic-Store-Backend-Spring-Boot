package com.shashwat.electronicstorebackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity
public class User {
	
	private static final boolean bool = true;

	@Id
	@Column(name = "user_id")
	private String id;
	
	@Column(name = "user_name")
	private String name;
	
	@Column(name = "sex")
	private String sex;
	
	@Column(name = "about", length = 500)
	private String about;
	
	@Column(name = "user_email", unique = bool)
	private String email;
	
	@Column(name = "user_password")
	private String password;
	
	@Column(name = "user_img")
	private String imageName;
	
}
