package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carts_seq")
    @SequenceGenerator(name = "carts_seq", sequenceName = "carts_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
