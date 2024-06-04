package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String brand;
    @Column
    private Double price;
    @Column
    private Integer stock;
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;
    @Column
    private String image;

}
