package com.gaurav.sales.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderDashboardResponse {

    private long totalOrders;

    private long pendingOrders;

    private long confirmedOrders;

    private long processingOrders;

    private long shippedOrders;

    private long outForDeliveryOrders;

    private long deliveredOrders;

    private long cancelledOrders;

    private BigDecimal totalRevenue;
}
