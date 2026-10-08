package com.gaurav.sales.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gaurav.sales.enums.OrderStatus;
import com.gaurav.sales.enums.PaymentMethod;
import com.gaurav.sales.enums.PaymentStatus;

@Entity
@Table(name = "orders", indexes = { @Index(name = "idx_order_number", columnList = "order_number"),
		@Index(name = "idx_order_phone", columnList = "phone_number"),
		@Index(name = "idx_order_created_at", columnList = "created_at") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_number", nullable = false, unique = true, length = 30)
	private String orderNumber;

	// =========================
	// CUSTOMER DETAILS
	// =========================

	@Column(name = "full_name", nullable = false, length = 100)
	private String fullName;

	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(length = 150)
	private String email;

	// =========================
	// DELIVERY ADDRESS
	// =========================

	@Column(name = "street_address", nullable = false, length = 300)
	private String streetAddress;

	@Column(nullable = false, length = 100)
	private String city;

	@Column(name = "postal_code", nullable = false, length = 10)
	private String postalCode;

	@Column(name = "country_region", nullable = false, length = 100)
	private String countryRegion;

	@Column(name = "delivery_instructions", length = 500)
	private String deliveryInstructions;

	// =========================
	// PAYMENT
	// =========================

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false, length = 30)
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false, length = 30)
	private PaymentStatus paymentStatus;

	// =========================
	// ORDER STATUS
	// =========================

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false, length = 30)
	private OrderStatus orderStatus;

	// =========================
	// PRICE
	// =========================

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal subtotal;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal deliveryCharge;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal totalAmount;

	// =========================
	// ORDER ITEMS
	// =========================

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<OrderItem> items = new ArrayList<>();

	// =========================
	// DATES
	// =========================

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {

		LocalDateTime now = LocalDateTime.now();

		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	protected void onUpdate() {

		updatedAt = LocalDateTime.now();
	}

	public void addItem(OrderItem item) {

		items.add(item);

		item.setOrder(this);
	}
}