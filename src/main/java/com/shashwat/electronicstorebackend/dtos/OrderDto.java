package com.shashwat.electronicstorebackend.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class OrderDto {

	private String orderId;
	
	private LocalDateTime createAt;

	private double orderAmount;

	private String orderStatus;
	
	private String paymentStatus;

	@NotBlank(message = "Billing name is required")
	private String billingName;
	
	@NotBlank(message = "Billing address is required")
	private String billingAddress;

	@NotBlank(message = "Billing phone is required")
	@Size(max = 10, min = 6, message = "Invalid phone no.")
	private String billingPhone;

	private UserDto user;
	
	private List<OrderItemDto> orderItems = new ArrayList<>();

}
