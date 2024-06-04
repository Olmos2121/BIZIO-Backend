package com.example.uade.tpo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_seq")
    @SequenceGenerator(name = "payments_seq", sequenceName = "payments_seq", allocationSize = 1)
    private Long id;
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;
    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
}
