package com.shashwat.electronicstorebackend.services;

import com.shashwat.electronicstorebackend.dtos.OrderDto;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;

public interface OrderService {

	// create an order
	OrderDto placeOrder(String userId, OrderDto orderDto);
	
	// delete an order
	void cancelOrder(String orderId);
	
	// update order
	OrderDto updateOrder(String orderId, OrderDto orderDto);
	
	// get all orders of an user
	PageableResponse<OrderDto> getOrdersOfUser(int pageNumber, int pageSize, String sortBy, String sortDir, String userId);
	
	// get all the orders
	PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
}
