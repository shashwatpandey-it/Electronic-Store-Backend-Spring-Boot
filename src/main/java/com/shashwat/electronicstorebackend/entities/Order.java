package com.shashwat.electronicstorebackend.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "created_at")
	private LocalDateTime createAt;
	
	@Column(name = "net_payable_amount")
	private double orderAmount;
	
	// PENDING / SHIPPED / DELIEVERED
	@Column(name = "order_status")
	private String orderStatus;
	
	// PAID / NOTPAID
	@Column(name = "payment_status")
	private String paymentStatus;
	
	@Column(name = "billing_name")
	private String billingName;
	
	@Column(name = "billing_address", length = 1000)
	private String billingAddress;
	
	@Column(name = "billing_phone")
	private String billingPhone;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OrderItem> orderItems = new ArrayList<>();
}
