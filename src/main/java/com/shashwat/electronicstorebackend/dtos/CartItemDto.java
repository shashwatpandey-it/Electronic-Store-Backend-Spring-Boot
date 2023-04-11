package com.shashwat.electronicstorebackend.dtos;

 

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
public class CartItemDto {

	private Integer id;
	private ProductDto product;
	private int quantity;
	private double purchaseValue;
}
