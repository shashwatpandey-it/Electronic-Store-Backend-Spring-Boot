package com.shashwat.electronicstorebackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shashwat.electronicstorebackend.dtos.CartDto;
import com.shashwat.electronicstorebackend.services.CartService;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
	
	@PostMapping("{userId}/product/{productId}")
	public ResponseEntity<CartDto> addToCart(@PathVariable String userId, @PathVariable String productId){
		CartDto cartDto = cartService.addItemToCart(userId, productId);
		LOGGER.info("----* PRODUCT ADDED TO CART *----");
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
	
	@DeleteMapping("{userId}/product/{productId}")
	public ResponseEntity<ResponseMessage> removeFromCart(@PathVariable String userId, @PathVariable String productId){
		cartService.removeItemFromCart(userId, productId);
		ResponseMessage message = ResponseMessage.builder()
													.message("item removed from the cart")
													.actionPerformed(true)
													.build();
		LOGGER.info("----* REMOVED FROM CART *----");
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK); 
	}
	
	@DeleteMapping("{userId}/clear")
	public ResponseEntity<ResponseMessage> clearCart(@PathVariable String userId){
		cartService.clearCart(userId);
		ResponseMessage message = ResponseMessage.builder()
													.message("cart cleared")
													.actionPerformed(true)
													.build();
		LOGGER.info("----* CART CLEARED *----");
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK); 
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> getCartOfUser(@PathVariable String userId){
		CartDto cartDto = cartService.getCart(userId);
		LOGGER.info("----* CART FETCHED *----");
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
}
