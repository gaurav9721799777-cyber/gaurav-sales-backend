package com.gaurav.sales.dtos;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gaurav.sales.enums.OrderStatus;
import com.gaurav.sales.enums.PaymentMethod;
import com.gaurav.sales.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private String orderNumber;


    // Customer
    private String fullName;

    private String phoneNumber;

    private String email;


    // Address
    private String streetAddress;

    private String city;

    private String postalCode;

    private String countryRegion;

    private String deliveryInstructions;


    // Payment
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;


    // Order
    private OrderStatus orderStatus;


    // Amount
    private BigDecimal subtotal;

    private BigDecimal deliveryCharge;

    private BigDecimal totalAmount;


    // Items
    private List<OrderItemResponse> items;


    // Dates
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
