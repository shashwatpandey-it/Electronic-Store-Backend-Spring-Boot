package com.shashwat.electronicstorebackend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	
	@Column(name = "about")
	private String about;
	
	@Column(name = "user_email", unique = bool)
	private String email;
	
	@Column(name = "user_password")
	private String password;
	
	@Column(name = "user_img")
	private String imageName;
	
}
