package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discounts_seq")
    @SequenceGenerator(name = "discounts_seq", sequenceName = "discounts_seq", allocationSize = 1)
    private Long id;
    @Column
    private String code;
    @Column
    private Integer discountValue;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
}
