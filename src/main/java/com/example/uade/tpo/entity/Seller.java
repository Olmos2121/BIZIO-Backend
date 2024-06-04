package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_seq")
    @SequenceGenerator(name = "seller_seq", sequenceName = "seller_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column
    private String storeName;
    @Column
    private String address;

}
