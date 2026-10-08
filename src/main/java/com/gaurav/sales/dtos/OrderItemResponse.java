package com.gaurav.sales.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String productSlug;

    private String imageUrl;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal itemTotal;
}