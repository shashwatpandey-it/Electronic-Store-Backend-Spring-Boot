package com.shashwat.electronicstorebackend.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name = "category")
public class Category {

	@Id
	@Column(name = "category_id")
	private String id;
	
	@Column(name = "category_title", nullable = false, unique = true)
	private String title;
	
	@Column(name = "cover_image")
	private String coverImageName;
	
	@ManyToMany(mappedBy = "categories", fetch =  FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Product> products = new HashSet<>();
}
