package com.gaurav.sales.serviceImpl;

import com.gaurav.sales.dtos.AddToCartRequest;
import com.gaurav.sales.dtos.CartItemResponse;
import com.gaurav.sales.dtos.CartResponse;
import com.gaurav.sales.dtos.UpdateCartItemRequest;
import com.gaurav.sales.entity.Cart;
import com.gaurav.sales.entity.CartItem;
import com.gaurav.sales.entity.Product;
import com.gaurav.sales.exceptions.ResourceNotFoundException;
import com.gaurav.sales.reposistory.CartItemRepository;
import com.gaurav.sales.reposistory.CartRepository;
import com.gaurav.sales.reposistory.ProductRepository;
import com.gaurav.sales.service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;

	private final CartItemRepository cartItemRepository;

	private final ProductRepository productRepository;

	// =========================================================
	// ADD TO CART
	// =========================================================

	@Override
	public CartResponse addToCart(String cartId, AddToCartRequest request) {

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

		// Check product active
		if (!Boolean.TRUE.equals(product.getActive())) {

			throw new IllegalStateException("Product is not available");
		}

		// Check stock
		if (product.getStockQuantity() == null) {

			throw new IllegalStateException("Product stock is not configured");
		}

		if (product.getStockQuantity() < request.getQuantity()) {

			throw new IllegalStateException("Only " + product.getStockQuantity() + " items are available in stock");
		}

		// Get or create cart
		Cart cart = cartRepository.findByCartId(cartId).orElseGet(() -> {

			Cart newCart = Cart.builder().cartId(cartId).items(new ArrayList<>()).build();

			return cartRepository.save(newCart);
		});

		// Find existing cart item
		CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_Id(cartId, product.getId()).orElse(null);

		if (cartItem != null) {

			int newQuantity = cartItem.getQuantity() + request.getQuantity();

			// Check total quantity against stock
			if (newQuantity > product.getStockQuantity()) {

				throw new IllegalStateException("Requested quantity exceeds available stock");
			}

			cartItem.setQuantity(newQuantity);

			cartItem.setPrice(getProductPrice(product));

			cartItemRepository.save(cartItem);

		} else {

			cartItem = CartItem.builder().cart(cart).product(product).quantity(request.getQuantity())
					.price(getProductPrice(product)).build();

			cartItemRepository.save(cartItem);
		}

		return getCart(cartId);
	}

	// =========================================================
	// GET CART
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public CartResponse getCart(String cartId) {

		Cart cart = cartRepository.findByCartId(cartId)
				.orElseGet(() -> Cart.builder().cartId(cartId).items(new ArrayList<>()).build());

		return mapToResponse(cart);
	}

	// =========================================================
	// UPDATE CART ITEM
	// =========================================================

	@Override
	public CartResponse updateCartItem(String cartId, Long productId, UpdateCartItemRequest request) {

		// Make sure cart exists
		getCartEntity(cartId);

		CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_Id(cartId, productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		Product product = cartItem.getProduct();

		// Check product active
		if (!Boolean.TRUE.equals(product.getActive())) {

			throw new IllegalStateException("Product is no longer available");
		}

		// Check stock
		if (product.getStockQuantity() == null) {

			throw new IllegalStateException("Product stock is not configured");
		}

		if (request.getQuantity() > product.getStockQuantity()) {

			throw new IllegalStateException("Only " + product.getStockQuantity() + " items are available");
		}

		cartItem.setQuantity(request.getQuantity());

		// Update current product price
		cartItem.setPrice(getProductPrice(product));

		cartItemRepository.save(cartItem);

		return getCart(cartId);
	}

	// =========================================================
	// REMOVE ITEM
	// =========================================================

	@Override
	public void removeCartItem(String cartId, Long productId) {

		// Make sure cart exists
		getCartEntity(cartId);

		CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_Id(cartId, productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		cartItemRepository.delete(cartItem);
	}

	// =========================================================
	// CLEAR CART
	// =========================================================

	@Override
	public void clearCart(String cartId) {

		// Make sure cart exists
		getCartEntity(cartId);

		cartItemRepository.deleteAllByCart_CartId(cartId);
	}

	// =========================================================
	// GET CART ENTITY
	// =========================================================

	private Cart getCartEntity(String cartId) {

		return cartRepository.findByCartId(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with cartId: " + cartId));
	}

	// =========================================================
	// GET PRODUCT PRICE
	// =========================================================

	private BigDecimal getProductPrice(Product product) {

		if (product.getDiscountPrice() != null && product.getPrice() != null
				&& product.getDiscountPrice().compareTo(product.getPrice()) < 0) {

			return product.getDiscountPrice();
		}

		if (product.getPrice() == null) {

			throw new IllegalStateException("Product price is not configured");
		}

		return product.getPrice();
	}

	// =========================================================
	// MAP ENTITY TO RESPONSE
	// =========================================================

	private CartResponse mapToResponse(Cart cart) {

		List<CartItemResponse> itemResponses = new ArrayList<>();

		BigDecimal subtotal = BigDecimal.ZERO;

		int totalItems = 0;

		/*
		 * For a newly-created cart that has not yet been saved, items can be null.
		 */
		List<CartItem> items = cart.getItems() != null ? cart.getItems() : new ArrayList<>();

		for (CartItem item : items) {

			Product product = item.getProduct();

			BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

			subtotal = subtotal.add(itemTotal);

			totalItems += item.getQuantity();

			CartItemResponse response = CartItemResponse.builder().id(item.getId()).productId(product.getId())
					.productName(product.getName()).productSlug(product.getSlug()).imageUrl(product.getImageUrl())
					.price(item.getPrice()).quantity(item.getQuantity()).itemTotal(itemTotal).build();

			itemResponses.add(response);
		}

		return CartResponse.builder().cartId(cart.getCartId()).items(itemResponses).totalItems(totalItems)
				.subtotal(subtotal).createdAt(cart.getCreatedAt()).updatedAt(cart.getUpdatedAt()).build();
	}
}