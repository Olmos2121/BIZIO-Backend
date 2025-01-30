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
    @Column(nullable = false, unique = true)
    private String name;
    @Column (nullable = false)
    private String description;
    @Column (nullable = false)
    private String brand;
    @Column (nullable = false)
    private String category;
    @Column(nullable = false)
    private Double price;
    @Column (nullable = false)
    private byte[] image;
    @Column(nullable = false)
    private Integer stock;
    @Column
    private boolean isInDiscount = false;
    @Column
    private Double discountPercentage;

    public double discountPrice() {
        price -= (price * discountPercentage / 100);
        return price;
    }
}
