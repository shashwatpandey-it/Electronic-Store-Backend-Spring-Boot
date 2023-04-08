package com.shashwat.electronicstorebackend.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.shashwat.electronicstorebackend.dtos.CategoryDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "products")
public class Product {

	@Id
	@Column(name = "product_id")
	private String id;
	private String manufacturer;
	@Column(name = "name", unique = true)
	private String name;
	@Column(length = 1500)
	private String description;
	@Column(name = "product_quantity")
	private int quantity;
	@Column(name = "listed_price", nullable = false)
	private float listedPrice;
	@Column(name = "discounted_price")
	private float discountedPrice;
	@Column(name = "listing_date")
	private LocalDate listingDate;
	private boolean stock;
	@Column(name = "product_image")
	private String imageName;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "prod_cat_association",
				joinColumns = {
						@JoinColumn(referencedColumnName = "product_id")
				},
				inverseJoinColumns = {
						@JoinColumn(referencedColumnName = "category_id")
				})
	Set<Category> categories = new HashSet<>();
	
	public boolean addCategory(Category category) {
		return categories.add(category);
	}
}
