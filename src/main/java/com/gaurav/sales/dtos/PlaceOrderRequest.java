package com.gaurav.sales.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequest {

	@NotBlank(message = "Full name is required")
	@Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
	private String fullName;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Please enter a valid 10 digit phone number")
	private String phoneNumber;

	@Email(message = "Please enter a valid email address")
	@Size(max = 150, message = "Email cannot exceed 150 characters")
	private String email;

	@NotBlank(message = "Street address is required")
	@Size(max = 300, message = "Street address cannot exceed 300 characters")
	private String streetAddress;

	@NotBlank(message = "City is required")
	@Size(max = 100, message = "City cannot exceed 100 characters")
	private String city;

	@NotBlank(message = "Postal code is required")
	@Pattern(regexp = "^[0-9]{6}$", message = "Postal code must be 6 digits")
	private String postalCode;

	@NotBlank(message = "Country / region is required")
	@Size(max = 100, message = "Country / region cannot exceed 100 characters")
	private String countryRegion;

	@Size(max = 500, message = "Delivery instructions cannot exceed 500 characters")
	private String deliveryInstructions;

	/*
	 * Currently only COD is supported.
	 */
	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
}