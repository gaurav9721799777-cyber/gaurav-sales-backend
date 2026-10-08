package com.gaurav.sales.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gaurav.sales.dtos.OrderDashboardResponse;
import com.gaurav.sales.dtos.OrderResponse;
import com.gaurav.sales.dtos.PlaceOrderRequest;
import com.gaurav.sales.exceptions.ApiResponse;
import com.gaurav.sales.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    // =========================================================
    // PLACE ORDER - CUSTOMER
    // =========================================================

    @PostMapping("/{cartId}")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @PathVariable String cartId,
            @Valid @RequestBody PlaceOrderRequest request) {

        OrderResponse order = orderService.placeOrder(cartId, request);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Order placed successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - GET ALL ORDERS
    // =========================================================

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrdersForAdmin() {

        List<OrderResponse> orders = orderService.getAllOrdersForAdmin();

        ApiResponse<List<OrderResponse>> response =
                ApiResponse.<List<OrderResponse>>builder()
                        .success(true)
                        .message("All orders fetched successfully")
                        .data(orders)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - GET ORDER BY ID
    // =========================================================

    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByIdForAdmin(
            @PathVariable Long id) {

        OrderResponse order = orderService.getOrderById(id);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Order fetched successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // CUSTOMER - GET ORDER BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long id) {

        OrderResponse order = orderService.getOrderById(id);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Order fetched successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ORDER BY ORDER NUMBER
    // =========================================================

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByNumber(
            @PathVariable String orderNumber) {

        OrderResponse order =
                orderService.getOrderByNumber(orderNumber);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Order fetched successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - GET ORDERS BY STATUS
    // =========================================================

    @GetMapping("/admin/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(
            @PathVariable String status) {

        List<OrderResponse> orders =
                orderService.getOrdersByStatus(status);

        ApiResponse<List<OrderResponse>> response =
                ApiResponse.<List<OrderResponse>>builder()
                        .success(true)
                        .message("Orders fetched successfully")
                        .data(orders)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - GET ORDERS BY PAYMENT STATUS
    // =========================================================

    @GetMapping("/admin/payment-status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByPaymentStatus(
            @PathVariable String status) {

        List<OrderResponse> orders =
                orderService.getOrdersByPaymentStatus(status);

        ApiResponse<List<OrderResponse>> response =
                ApiResponse.<List<OrderResponse>>builder()
                        .success(true)
                        .message("Orders fetched successfully")
                        .data(orders)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - UPDATE ORDER STATUS
    // =========================================================

    @PatchMapping("/admin/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        OrderResponse order =
                orderService.updateOrderStatus(id, status);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Order status updated successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - UPDATE PAYMENT STATUS
    // =========================================================

    @PatchMapping("/admin/{id}/payment-status")
    public ResponseEntity<ApiResponse<OrderResponse>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        OrderResponse order =
                orderService.updatePaymentStatus(id, status);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Payment status updated successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ADMIN - CANCEL ORDER
    // =========================================================

    @PatchMapping("/admin/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long id) {

        OrderResponse order =
                orderService.cancelOrder(id);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Order cancelled successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin/count")
    public ResponseEntity<ApiResponse<Long>> getOrderCount() {

        Long count = orderService.getOrderCount();

        ApiResponse<Long> response =
                ApiResponse.<Long>builder()
                        .success(true)
                        .message("Order count fetched successfully")
                        .data(count)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin/revenue")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalRevenue() {

        BigDecimal revenue = orderService.getTotalRevenue();

        ApiResponse<BigDecimal> response =
                ApiResponse.<BigDecimal>builder()
                        .success(true)
                        .message("Total revenue fetched successfully")
                        .data(revenue)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin/count/status/{status}")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByStatus(
            @PathVariable String status) {

        long count = orderService.getOrderCountByStatus(status);

        ApiResponse<Long> response =
                ApiResponse.<Long>builder()
                        .success(true)
                        .message("Order count by status fetched successfully")
                        .data(count)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/admin/dashboard")
    public ResponseEntity<ApiResponse<OrderDashboardResponse>> getOrderDashboard() {

        OrderDashboardResponse dashboard =
                orderService.getOrderDashboard();

        ApiResponse<OrderDashboardResponse> response =
                ApiResponse.<OrderDashboardResponse>builder()
                        .success(true)
                        .message("Order dashboard fetched successfully")
                        .data(dashboard)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}