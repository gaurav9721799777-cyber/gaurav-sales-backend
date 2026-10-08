package com.gaurav.sales.dtos;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

	@NotBlank(message = "Product name is required")
	private String name;

	@NotBlank(message = "Product slug is required")
	private String slug;

	private String description;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private BigDecimal price;

	@DecimalMin(value = "0.0", inclusive = true, message = "Discount price cannot be negative")
	private BigDecimal discountPrice;

	@NotNull(message = "Stock quantity is required")
	@Min(value = 0, message = "Stock cannot be negative")
	private Integer stockQuantity;

	private String sku;

	@NotNull(message = "Brand is required")
	private Long brandId;

	@NotNull(message = "Category is required")
	private Long categoryId;

	// Product image
	private MultipartFile image;
}