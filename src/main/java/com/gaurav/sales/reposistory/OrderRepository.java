package com.gaurav.sales.reposistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaurav.sales.entity.Order;
import com.gaurav.sales.enums.OrderStatus;
import com.gaurav.sales.enums.PaymentStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByOrderNumber(String orderNumber);

	boolean existsByOrderNumber(String orderNumber);

	List<Order> findByOrderStatus(OrderStatus orderStatus);

	List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
	
	long countByOrderStatus(OrderStatus orderStatus);
}
