package com.shashwat.electronicstorebackend.dtos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.shashwat.electronicstorebackend.utilities.ValidImage;

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
public class ProductDto {

	private String id;
	@NotBlank(message = "Product manufacturer is required")
	private String manufacturer;
	@NotBlank(message = "Product name can not be blank")
	private String name;
	private String description;
	private int quantity;
	private float listedPrice;
	private float discountedPrice;
	private LocalDate listingDate;
	private boolean stock;
	@ValidImage(message = "Not a valid image name")
	private String imageName;
	private Set<CategoryDto> categoriesSet = new HashSet<>(); 
	
	public boolean addCategory(CategoryDto categoryDto) {
		return categoriesSet.add(categoryDto);
	}
}
