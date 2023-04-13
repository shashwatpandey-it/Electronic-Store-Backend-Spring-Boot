package com.shashwat.electronicstorebackend.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shashwat.electronicstorebackend.dtos.CartDto;
import com.shashwat.electronicstorebackend.entities.Cart;
import com.shashwat.electronicstorebackend.entities.CartItem;
import com.shashwat.electronicstorebackend.entities.Product;
import com.shashwat.electronicstorebackend.entities.User;
import com.shashwat.electronicstorebackend.exceptions.ResourceNotFoundException;
import com.shashwat.electronicstorebackend.repositories.CartRepository;
import com.shashwat.electronicstorebackend.repositories.ProductRepository;
import com.shashwat.electronicstorebackend.repositories.UserRepository;
import com.shashwat.electronicstorebackend.services.CartService;



@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public CartDto addItemToCart(String userId, String productId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesn't exists"));
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No such product exist"));
		
		// getting the cart associated with user or if the cart not exists then creating a cart for the user
		Cart cart  = null;
		try {
			cart = cartRepository.findByUser(user).get();
		} catch (NoSuchElementException e) {
			List<CartItem> cartItems = new ArrayList<>();
			cart = Cart.builder()
						.cartId(UUID.randomUUID().toString())
						.createdAt(LocalDateTime.now())
						.user(user)
						.cartItems(cartItems)
						.build();
			// TODO: handle exception
		}
		
		// if product is already present then increment the qty by 1 else initialize qty by 1;
		AtomicBoolean updated = new AtomicBoolean();
		List<CartItem> updatedCartItems = cart.getCartItems().stream().map(item -> {
			if(item.getProduct().equals(product)) {
				item.setQuantity(item.getQuantity()+1);
				item.setPurchaseValue(item.getProduct().getDiscountedPrice() * item.getQuantity());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());
		cart.getCartItems().clear();
		cart.getCartItems().addAll(updatedCartItems);
		
		if(!updated.get()) {
			CartItem cartItem = CartItem.builder()
										.product(product)
										.quantity(1)
										.purchaseValue(product.getDiscountedPrice())
										.cart(cart)
										.build();
			cart.getCartItems().add(cartItem);
		}
		
		Cart savedCart = cartRepository.save(cart);
		return mapper.map(savedCart, CartDto.class);
	}

	@Transactional
	@Override
	public void removeItemFromCart(String userId, String productId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesn't exists"));
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No such product exist"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No cart exists! Try creating a cart before removing from cart"));
		
		// if qty > 1 then remove 1 product of that type from cart else remove product
		AtomicBoolean updated = new AtomicBoolean(); 
		AtomicInteger removalIndex = new AtomicInteger(); 
		List<CartItem> updatedCartItems = cart.getCartItems().stream().map(item -> {
			if(item.getProduct().equals(product)) {
				if(item.getQuantity() > 1) {
					item.setQuantity(item.getQuantity() - 1);
					item.setPurchaseValue(item.getProduct().getDiscountedPrice() * item.getQuantity());
					updated.set(true);
				}
				else if(item.getQuantity() == 1) {
					removalIndex.set(cart.getCartItems().indexOf(item));
				}
			}
			return item;
		}).collect(Collectors.toList());

		if(!updated.get()) {
			updatedCartItems.remove(removalIndex.get());
		}
		
		cart.getCartItems().clear();
		cart.getCartItems().addAll(updatedCartItems);
		cartRepository.save(cart);
	}

	@Transactional
	@Override
	public void clearCart(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesn't exists"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No cart exists! Try creating a cart before clearing cart"));
		cart.getCartItems().clear();
		cartRepository.save(cart);
	}

	@Override
	public CartDto getCart(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesn't exists"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No cart exists! Try creating a cart before clearing cart"));
		
		return mapper.map(cart, CartDto.class);
	}

}
