package com.example.uade.tpo.repository;

import com.example.uade.tpo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p.id FROM Payment p")
    List<Long> getAllPaymentIds();
    Payment findByOrderId(Long orderId);

    List<Payment> findAllByOrderId(Long id);
}
