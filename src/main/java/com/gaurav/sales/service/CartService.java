package com.gaurav.sales.service;

import com.gaurav.sales.dtos.AddToCartRequest;
import com.gaurav.sales.dtos.CartResponse;
import com.gaurav.sales.dtos.UpdateCartItemRequest;

public interface CartService {

	CartResponse addToCart(String cartId, AddToCartRequest request);

	CartResponse getCart(String cartId);

	CartResponse updateCartItem(String cartId, Long productId, UpdateCartItemRequest request);

	void removeCartItem(String cartId, Long productId);

	void clearCart(String cartId);
}