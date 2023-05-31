package com.shashwat.electronicstorebackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shashwat.electronicstorebackend.dtos.OrderDto;
import com.shashwat.electronicstorebackend.services.OrderService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Module", description = "Endpoints for order management")
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/{userId}")
	@Operation(
				summary = "Place order",
				description = "Place order for items in the cart."
			)
	public ResponseEntity<OrderDto> placeOrder(@PathVariable String userId, @Valid @RequestBody OrderDto orderDto){
		OrderDto placedOrderDto = orderService.placeOrder(userId, orderDto);
		LOGGER.info("----* ORDER PLACED *----");
		return new ResponseEntity<OrderDto>(placedOrderDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{orderId}")
	@Operation(
				summary = "Cancel order",
				description = "Cancel a specific order by order id."
			)
	public ResponseEntity<ResponseMessage> cancelOrder(@PathVariable String orderId){
		orderService.cancelOrder(orderId);
		ResponseMessage message = ResponseMessage.builder()
													.message("Order with id : "+ orderId + " successfully deleted")
													.actionPerformed(true)
													.build();
		LOGGER.info("----* ORDER DELETED *----");
		return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	@Operation(
				summary = "Get orders of user",
				description = "fetch all oders of a user by his/her user id.(Pagination and sorting implemented)"
			)
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrdersOfUser(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "createAt", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "desc", required = false) String sortDir,
			@PathVariable String userId)
	{
		PageableResponse<OrderDto> list = orderService.getOrdersOfUser(pageNumber, pageSize, sortBy, sortDir, userId);
		LOGGER.info("----* ORDERS OF USER FETCHED *----");
		return new ResponseEntity<PageableResponse<OrderDto>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/all")
	@Operation(
			summary = "Get all orders",
			description = "fetch all oders in the system. This operation can only be performed by admin.(Pagination and sorting implemented)"
		)
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
			@RequestParam (name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam (name = "sortBy", defaultValue = "createAt", required = false) String sortBy,
			@RequestParam (name = "sortDir", defaultValue = "desc", required = false) String sortDir)
	{
		PageableResponse<OrderDto> list = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		LOGGER.info("----* ALL ORDERS FETCHED *----");
		return new ResponseEntity<PageableResponse<OrderDto>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{orderId}")
	@Operation(
				summary = "Update order",
				description = "Update placed orders for processing of order. This operation can only be performed by admin."
			)
	public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @Valid @RequestBody OrderDto orderDto){
		OrderDto updatedOrderDto = orderService.updateOrder(orderId, orderDto);
		LOGGER.info("----* ORDER UPDATED *----");
		return new ResponseEntity<OrderDto>(updatedOrderDto,HttpStatus.OK);
	}
}

