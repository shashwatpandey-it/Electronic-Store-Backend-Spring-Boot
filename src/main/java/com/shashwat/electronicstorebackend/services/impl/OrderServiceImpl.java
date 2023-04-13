package com.shashwat.electronicstorebackend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shashwat.electronicstorebackend.dtos.OrderDto;
import com.shashwat.electronicstorebackend.entities.Cart;
import com.shashwat.electronicstorebackend.entities.CartItem;
import com.shashwat.electronicstorebackend.entities.Order;
import com.shashwat.electronicstorebackend.entities.OrderItem;
import com.shashwat.electronicstorebackend.entities.User;
import com.shashwat.electronicstorebackend.exceptions.BadApiRequestException;
import com.shashwat.electronicstorebackend.exceptions.ResourceNotFoundException;
import com.shashwat.electronicstorebackend.repositories.CartRepository;
import com.shashwat.electronicstorebackend.repositories.OrderRepository;
import com.shashwat.electronicstorebackend.repositories.ProductRepository;
import com.shashwat.electronicstorebackend.repositories.UserRepository;
import com.shashwat.electronicstorebackend.services.OrderService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Transactional
	@Override
	public OrderDto placeOrder(String userId, OrderDto orderDto) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No cart exists for user"));
		
		List<CartItem> cartItems = cart.getCartItems();
		
		if(cartItems.size() <= 0) throw new BadApiRequestException("Cart is empty. No items to place order");
		
		Order order = Order.builder()
							.orderId(UUID.randomUUID().toString())
							.createAt(LocalDateTime.now())
							.orderStatus("PENDING")
							.paymentStatus("NOTPAID")
							.billingName(orderDto.getBillingName())
							.billingAddress(orderDto.getBillingAddress())
							.billingPhone(orderDto.getBillingPhone())
							.user(user)
							.build();
							
		AtomicReference<Double> orderAmount = new AtomicReference<>(0.0d);
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			int stockQty = productRepository.findById(cartItem.getProduct().getId()).get().getQuantity();
			if(cartItem.getQuantity() > stockQty) {
				throw new BadApiRequestException("Sorry insufficient inventory!! Order can not be placed");
			}
			OrderItem orderItem = OrderItem.builder()
											.product(cartItem.getProduct())
											.quantity(cartItem.getQuantity())
											.purchaseValue(cartItem.getPurchaseValue())
											.order(order)
											.build();
			orderAmount.set(orderAmount.get() + orderItem.getPurchaseValue());
			productRepository.updateProductSetQuantityForId(stockQty - cartItem.getQuantity(), cartItem.getProduct().getId());
			return orderItem;
		}).collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		
		cart.getCartItems().clear();
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);
		return mapper.map(savedOrder, OrderDto.class);
	}

	@Transactional
	@Override
	public void cancelOrder(String orderId) {
		// TODO Auto-generated method stub
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("No order exists for user"));
		order.getOrderItems().stream().map(item -> {
			int stockQty = productRepository.findById(item.getProduct().getId()).get().getQuantity();
			productRepository.updateProductSetQuantityForId(stockQty + item.getQuantity(), item.getProduct().getId());
			return item;
		}).collect(Collectors.toList());
		orderRepository.delete(order);	
	}

	@Override
	public PageableResponse<OrderDto> getOrdersOfUser(int pageNumber, int pageSize, String sortBy, String sortDir, String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> page = orderRepository.findAllByUser(pageable, user);
		PageableResponse<OrderDto> pageableResponse = PageableResponse.getPageableResponse(OrderDto.class, page);
		return pageableResponse;
	}

	@Override
	public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> page = orderRepository.findAll(pageable);
		PageableResponse<OrderDto> pageableResponse = PageableResponse.getPageableResponse(OrderDto.class, page);
		return pageableResponse;
	}

	@Override
	public OrderDto updateOrder(String orderId, OrderDto orderDto) {
		// TODO Auto-generated method stub
		Order oldOrder = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("No such order exists"));
		oldOrder.setOrderStatus(orderDto.getOrderStatus());
		oldOrder.setPaymentStatus(orderDto.getPaymentStatus());
		Order savedOrder = orderRepository.save(oldOrder);
		return mapper.map(savedOrder, OrderDto.class);
	}

}
