package com.shashwat.electronicstorebackend.services;

import com.shashwat.electronicstorebackend.dtos.CartDto;

public interface CartService {

	// Use case 1 : ADD ITEMS TO CART
	// condition 1 : if cart for user is not available -> create the cart and then add item to cart
	// condition 2 : else add the items to cart
	
	CartDto addItemToCart(String userId, String productId);
	
	void removeItemFromCart(String userId, String productId);
	
	void clearCart(String userId);
	
	CartDto getCart(String userId);
	
}
