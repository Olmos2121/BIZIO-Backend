package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_details_seq")
    @SequenceGenerator(name = "order_details_seq", sequenceName = "order_details_seq", allocationSize = 1)
    private Long id;
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column
    private Integer quantity;
    @Column
    private Double price;
    @Column
    private Double total;
}
