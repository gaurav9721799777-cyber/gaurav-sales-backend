
package com.gaurav.sales.service;

import com.gaurav.sales.dtos.OrderDashboardResponse;
import com.gaurav.sales.dtos.OrderResponse;
import com.gaurav.sales.dtos.PlaceOrderRequest;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    // =========================
    // CUSTOMER
    // =========================

    OrderResponse placeOrder(
            String cartId,
            PlaceOrderRequest request
    );

    OrderResponse getOrderById(Long id);

    OrderResponse getOrderByNumber(String orderNumber);


    // =========================
    // ADMIN
    // =========================

    List<OrderResponse> getAllOrdersForAdmin();

    List<OrderResponse> getOrdersByStatus(
            String status
    );

    List<OrderResponse> getOrdersByPaymentStatus(
            String status
    );

    OrderResponse updateOrderStatus(
            Long id,
            String status
    );

    OrderResponse updatePaymentStatus(
            Long id,
            String status
    );

    OrderResponse cancelOrder(
            Long id
    );

    Long getOrderCount();

    long getOrderCountByStatus(
            String status
    );

    BigDecimal getTotalRevenue();

    OrderDashboardResponse getOrderDashboard();
}
