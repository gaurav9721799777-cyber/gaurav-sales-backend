package com.gaurav.sales.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaurav.sales.dtos.OrderDashboardResponse;
import com.gaurav.sales.dtos.OrderResponse;
import com.gaurav.sales.dtos.PlaceOrderRequest;
import com.gaurav.sales.entity.Cart;
import com.gaurav.sales.entity.CartItem;
import com.gaurav.sales.entity.Order;
import com.gaurav.sales.entity.OrderItem;
import com.gaurav.sales.entity.Product;
import com.gaurav.sales.enums.OrderStatus;
import com.gaurav.sales.enums.PaymentMethod;
import com.gaurav.sales.enums.PaymentStatus;
import com.gaurav.sales.exceptions.ResourceNotFoundException;
import com.gaurav.sales.reposistory.CartItemRepository;
import com.gaurav.sales.reposistory.CartRepository;
import com.gaurav.sales.reposistory.OrderRepository;
import com.gaurav.sales.reposistory.ProductRepository;
import com.gaurav.sales.service.OrderService;
import com.gaurav.sales.utility.AzamgarhPostalCodeValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final CartRepository cartRepository;

	private final CartItemRepository cartItemRepository;

	private final ProductRepository productRepository;

	// =========================================================
	// PLACE ORDER
	// =========================================================

	@Override
	public OrderResponse placeOrder(String cartId, PlaceOrderRequest request) {

		AzamgarhPostalCodeValidator.isValid(request.getCity(), request.getPostalCode());
		
	
		Cart cart = cartRepository.findByCartId(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

		// =====================================================
		// 3. GET CART ITEMS
		// =====================================================

		List<CartItem> cartItems = cartItemRepository.findAllByCart_CartId(cartId);

		if (cartItems == null || cartItems.isEmpty()) {

			throw new ResourceNotFoundException("Cart is empty");
		}

		// =====================================================
		// 4. CREATE ORDER
		// =====================================================

		Order order = new Order();

		order.setOrderNumber(generateOrderNumber());

		// Customer
		order.setFullName(request.getFullName());

		order.setPhoneNumber(request.getPhoneNumber());

		order.setEmail(request.getEmail());

		// Address
		order.setStreetAddress(request.getStreetAddress());

		order.setCity(request.getCity());

		order.setPostalCode(request.getPostalCode());

		order.setCountryRegion(request.getCountryRegion());

		order.setDeliveryInstructions(request.getDeliveryInstructions());

		// =====================================================
		// 5. PAYMENT METHOD
		// =====================================================

		PaymentMethod paymentMethod;

		try {

			paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().trim().toUpperCase());

		} catch (ResourceNotFoundException e) {

			throw new ResourceNotFoundException("Invalid payment method: " + request.getPaymentMethod());
		}

		order.setPaymentMethod(paymentMethod);

		order.setPaymentStatus(PaymentStatus.PENDING);

		order.setOrderStatus(OrderStatus.PENDING);

		// =====================================================
		// 6. CALCULATE SUBTOTAL
		// =====================================================

		BigDecimal subtotal = BigDecimal.ZERO;

		List<OrderItem> orderItems = new ArrayList<>();

		// =====================================================
		// 7. PROCESS CART ITEMS
		// =====================================================

		for (CartItem cartItem : cartItems) {

			Product product = cartItem.getProduct();

			if (product == null) {

				throw new ResourceNotFoundException("Product not found in cart");
			}

			// Product active?
			if (!Boolean.TRUE.equals(product.getActive())) {

				throw new ResourceNotFoundException("Product is no longer available: " + product.getName());
			}

			// Quantity
			Integer quantity = cartItem.getQuantity();

			if (quantity == null || quantity <= 0) {

				throw new ResourceNotFoundException("Invalid quantity for product: " + product.getName());
			}

			// Stock
			if (product.getStockQuantity() == null || product.getStockQuantity() < quantity) {

				throw new ResourceNotFoundException("Insufficient stock for product: " + product.getName());
			}

			// =================================================
			// CURRENT SELLING PRICE
			// =================================================

			BigDecimal price;

			if (product.getDiscountPrice() != null && product.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0
					&& product.getDiscountPrice().compareTo(product.getPrice()) < 0) {

				price = product.getDiscountPrice();

			} else {

				price = product.getPrice();
			}

			// =================================================
			// ITEM TOTAL
			// =================================================

			BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

			subtotal = subtotal.add(itemTotal);

			// =================================================
			// CREATE ORDER ITEM
			// =================================================

			OrderItem orderItem = new OrderItem();

			orderItem.setOrder(order);

			orderItem.setProduct(product);

			orderItem.setProductName(product.getName());

			orderItem.setProductSlug(product.getSlug());

			orderItem.setImageUrl(product.getImageUrl());

			orderItem.setPrice(price);

			orderItem.setQuantity(quantity);

			orderItem.setItemTotal(itemTotal);

			orderItems.add(orderItem);

			// =================================================
			// REDUCE STOCK
			// =================================================

			product.setStockQuantity(product.getStockQuantity() - quantity);

			productRepository.save(product);
		}

		// =====================================================
		// 8. DELIVERY CHARGE
		// =====================================================

		BigDecimal deliveryCharge = BigDecimal.ZERO;

		// =====================================================
		// 9. TOTAL
		// =====================================================

		BigDecimal totalAmount = subtotal.add(deliveryCharge);

		order.setSubtotal(subtotal);

		order.setDeliveryCharge(deliveryCharge);

		order.setTotalAmount(totalAmount);

		// =====================================================
		// 10. ORDER ITEMS
		// =====================================================

		order.setItems(orderItems);

		// =====================================================
		// 11. SAVE ORDER
		// =====================================================

		Order savedOrder = orderRepository.save(order);

		// =====================================================
		// 12. CLEAR CART
		// =====================================================

		cartItemRepository.deleteAllByCart_CartId(cartId);

		// =====================================================
		// 13. RETURN
		// =====================================================

		return mapToResponse(savedOrder);
	}

	// =========================================================
	// GET ORDER BY ID
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(Long id) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

		return mapToResponse(order);
	}

	// =========================================================
	// GET ORDER BY ORDER NUMBER
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderByNumber(String orderNumber) {

		Order order = orderRepository.findByOrderNumber(orderNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));

		return mapToResponse(order);
	}

	// =========================================================
	// ADMIN - GET ALL ORDERS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getAllOrdersForAdmin() {

		return orderRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	// =========================================================
	// ADMIN - GET ORDERS BY STATUS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getOrdersByStatus(String status) {

		OrderStatus orderStatus;

		try {

			orderStatus = OrderStatus.valueOf(status.toUpperCase());

		} catch (ResourceNotFoundException e) {

			throw new ResourceNotFoundException("Invalid order status: " + status);
		}

		return orderRepository.findByOrderStatus(orderStatus).stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	// =========================================================
	// ADMIN - GET ORDERS BY PAYMENT STATUS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getOrdersByPaymentStatus(String status) {

		PaymentStatus paymentStatus;

		try {

			paymentStatus = PaymentStatus.valueOf(status.toUpperCase());

		} catch (ResourceNotFoundException e) {

			throw new ResourceNotFoundException("Invalid payment status: " + status);
		}

		return orderRepository.findByPaymentStatus(paymentStatus).stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	// =========================================================
	// ADMIN - UPDATE ORDER STATUS
	// =========================================================

	@Override
	public OrderResponse updateOrderStatus(Long id, String status) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

		OrderStatus newStatus;

		try {

			newStatus = OrderStatus.valueOf(status.toUpperCase());

		} catch (ResourceNotFoundException e) {

			throw new ResourceNotFoundException("Invalid order status: " + status);
		}

		// Don't allow changing cancelled order
		if (order.getOrderStatus() == OrderStatus.CANCELLED) {

			throw new ResourceNotFoundException("Cancelled order status cannot be changed");
		}

		// Don't allow changing delivered order
		if (order.getOrderStatus() == OrderStatus.DELIVERED) {

			throw new ResourceNotFoundException("Delivered order status cannot be changed");
		}

		order.setOrderStatus(newStatus);

		Order updatedOrder = orderRepository.save(order);

		return mapToResponse(updatedOrder);
	}

	// =========================================================
	// ADMIN - UPDATE PAYMENT STATUS
	// =========================================================

	@Override
	public OrderResponse updatePaymentStatus(Long id, String status) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

		PaymentStatus newStatus;

		try {

			newStatus = PaymentStatus.valueOf(status.toUpperCase());

		} catch (ResourceNotFoundException e) {

			throw new ResourceNotFoundException("Invalid payment status: " + status);
		}

		order.setPaymentStatus(newStatus);

		Order updatedOrder = orderRepository.save(order);

		return mapToResponse(updatedOrder);
	}

	// =========================================================
	// ADMIN - CANCEL ORDER
	// =========================================================

	@Override
	public OrderResponse cancelOrder(Long id) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

		if (order.getOrderStatus() == OrderStatus.DELIVERED) {

			throw new ResourceNotFoundException("Delivered order cannot be cancelled");
		}

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {

			throw new ResourceNotFoundException("Order is already cancelled");
		}

		order.setOrderStatus(OrderStatus.CANCELLED);

		Order cancelledOrder = orderRepository.save(order);

		return mapToResponse(cancelledOrder);
	}

	// =========================================================
	// MAP ENTITY -> RESPONSE
	// =========================================================

	private OrderResponse mapToResponse(Order order) {

		return OrderResponse.builder()

				.id(order.getId())

				.orderNumber(order.getOrderNumber())

				.fullName(order.getFullName())

				.phoneNumber(order.getPhoneNumber())

				.email(order.getEmail())

				.streetAddress(order.getStreetAddress())

				.city(order.getCity())

				.postalCode(order.getPostalCode())

				.countryRegion(order.getCountryRegion())

				.deliveryInstructions(order.getDeliveryInstructions())

				.paymentMethod(order.getPaymentMethod())

				.paymentStatus(order.getPaymentStatus())

				.orderStatus(order.getOrderStatus())

				.subtotal(order.getSubtotal())

				.deliveryCharge(order.getDeliveryCharge())

				.totalAmount(order.getTotalAmount())

				.createdAt(order.getCreatedAt())

				.updatedAt(order.getUpdatedAt())

				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public Long getOrderCount() {
		return orderRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalRevenue() {

		return orderRepository.findAll().stream().filter(order -> order.getOrderStatus() != OrderStatus.CANCELLED)
				.map(Order::getTotalAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	@Transactional(readOnly = true)
	public long getOrderCountByStatus(String status) {

		OrderStatus orderStatus;

		try {
			orderStatus = OrderStatus.valueOf(status.toUpperCase());
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("Invalid order status: " + status);
		}

		return orderRepository.countByOrderStatus(orderStatus);
	}

	@Override
	@Transactional(readOnly = true)
	public OrderDashboardResponse getOrderDashboard() {

		long totalOrders = orderRepository.count();

		long pendingOrders = orderRepository.countByOrderStatus(OrderStatus.PENDING);

		long confirmedOrders = orderRepository.countByOrderStatus(OrderStatus.CONFIRMED);

		long processingOrders = orderRepository.countByOrderStatus(OrderStatus.PROCESSING);

		long shippedOrders = orderRepository.countByOrderStatus(OrderStatus.SHIPPED);

		long outForDeliveryOrders = orderRepository.countByOrderStatus(OrderStatus.OUT_FOR_DELIVERY);

		long deliveredOrders = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);

		long cancelledOrders = orderRepository.countByOrderStatus(OrderStatus.CANCELLED);

		BigDecimal totalRevenue = orderRepository.findAll().stream()
				.filter(order -> order.getOrderStatus() != OrderStatus.CANCELLED).map(Order::getTotalAmount)
				.filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

		return OrderDashboardResponse.builder().totalOrders(totalOrders).pendingOrders(pendingOrders)
				.confirmedOrders(confirmedOrders).processingOrders(processingOrders).shippedOrders(shippedOrders)
				.outForDeliveryOrders(outForDeliveryOrders).deliveredOrders(deliveredOrders)
				.cancelledOrders(cancelledOrders).totalRevenue(totalRevenue).build();
	}
	
	// =========================================================
    // ORDER NUMBER
    // =========================================================

    private String generateOrderNumber() {

        return "GS-"
                + System.currentTimeMillis()
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 4)
                        .toUpperCase();
    }
}