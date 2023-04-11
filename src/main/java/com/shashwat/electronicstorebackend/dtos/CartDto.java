package com.shashwat.electronicstorebackend.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class CartDto {

	private String cartId;
	private LocalDateTime createdAt;
	private UserDto user;
	private List<CartItemDto> cartItems = new ArrayList<>();
	
}
