package com.gaurav.sales.controller;

import com.gaurav.sales.dtos.AddToCartRequest;
import com.gaurav.sales.dtos.CartResponse;
import com.gaurav.sales.dtos.UpdateCartItemRequest;
import com.gaurav.sales.exceptions.ApiResponse;
import com.gaurav.sales.service.CartService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CartController {

	private final CartService cartService;

	// =========================================================
	// ADD PRODUCT TO CART
	// =========================================================

	@PostMapping("/{cartId}/items")
	public ResponseEntity<ApiResponse<CartResponse>> addToCart(@PathVariable String cartId,@Valid @RequestBody AddToCartRequest request) {

		CartResponse cart = cartService.addToCart(cartId, request);
		ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder().success(true)
				.message("Product added to cart successfully").data(cart).timestamp(LocalDateTime.now()).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// =========================================================
	// GET CART
	// =========================================================

	@GetMapping("/{cartId}")
	public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable String cartId) {

		CartResponse cart = cartService.getCart(cartId);
		ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder().success(true)
				.message("Cart fetched successfully").data(cart).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// =========================================================
	// UPDATE QUANTITY
	// =========================================================

	@PutMapping("/{cartId}/items/{productId}")
	public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(

			@PathVariable String cartId,

			@PathVariable Long productId,

			@Valid @RequestBody UpdateCartItemRequest request) {

		CartResponse cart = cartService.updateCartItem(cartId, productId, request);

		ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder().success(true)
				.message("Cart item updated successfully").data(cart).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// =========================================================
	// REMOVE ITEM
	// =========================================================

	@DeleteMapping("/{cartId}/items/{productId}")
	public ResponseEntity<ApiResponse<Void>> removeCartItem(@PathVariable String cartId,@PathVariable Long productId) {

		cartService.removeCartItem(cartId, productId);
		ApiResponse<Void> response = ApiResponse.<Void>builder().success(true).message("Product removed from cart")
				.data(null).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// =========================================================
	// CLEAR CART
	// =========================================================

	@DeleteMapping("/{cartId}/items")
	public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable String cartId) {

		cartService.clearCart(cartId);
		ApiResponse<Void> response = ApiResponse.<Void>builder().success(true).message("Cart cleared successfully")
				.data(null).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}
}