package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products_categories")
public class ProductsCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_categories_seq")
    @SequenceGenerator(name = "products_categories_seq", sequenceName = "products_categories_seq", allocationSize = 1)
    private Long id;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}
