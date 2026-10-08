package com.gaurav.sales.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String slug;

    private String sku;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private Integer stock;

    private Boolean active;

    private String description;

    private String mainImageUrl;

    private Long brandId;

    private String brandName;

    private String brandSlug;

    private Long categoryId;

    private String categoryName;

    private String categorySlug;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
