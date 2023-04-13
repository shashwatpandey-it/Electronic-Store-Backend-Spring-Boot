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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shashwat.electronicstorebackend.dtos.OrderDto;
import com.shashwat.electronicstorebackend.services.OrderService;
import com.shashwat.electronicstorebackend.utilities.PageableResponse;
import com.shashwat.electronicstorebackend.utilities.ResponseMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/{userId}")
	public ResponseEntity<OrderDto> placeOrder(@PathVariable String userId, @Valid @RequestBody OrderDto orderDto){
		OrderDto placedOrderDto = orderService.placeOrder(userId, orderDto);
		LOGGER.info("----* ORDER PLACED *----");
		return new ResponseEntity<OrderDto>(placedOrderDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{orderId}")
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
	
	@GetMapping("/all")
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
	
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @Valid @RequestBody OrderDto orderDto){
		OrderDto updatedOrderDto = orderService.updateOrder(orderId, orderDto);
		LOGGER.info("----* ORDER UPDATED *----");
		return new ResponseEntity<OrderDto>(updatedOrderDto,HttpStatus.OK);
	}
}

