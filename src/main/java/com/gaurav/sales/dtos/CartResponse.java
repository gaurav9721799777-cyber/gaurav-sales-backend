package com.gaurav.sales.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private String cartId;

    private List<CartItemResponse> items;

    private Integer totalItems;

    private BigDecimal subtotal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}