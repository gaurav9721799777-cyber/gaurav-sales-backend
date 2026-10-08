package com.gaurav.sales.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "order_items"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "product_id"
    )
    private Product product;


    @Column(
            name = "product_name",
            nullable = false,
            length = 200
    )
    private String productName;


    @Column(
            name = "product_slug",
            length = 250
    )
    private String productSlug;


    @Column(
            name = "image_url",
            length = 500
    )
    private String imageUrl;


    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal price;


    @Column(
            nullable = false
    )
    private Integer quantity;


    @Column(
            name = "item_total",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal itemTotal;
}