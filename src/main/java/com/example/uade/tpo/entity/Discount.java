package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discounts_seq")
    @SequenceGenerator(name = "discounts_seq", sequenceName = "discounts_seq", allocationSize = 1)
    private Long id;
    @Getter
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private Double percentage;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private boolean active;
}
