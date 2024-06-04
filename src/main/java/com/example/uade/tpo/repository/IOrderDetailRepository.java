package com.example.uade.tpo.repository;

import com.example.uade.tpo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    void deleteByOrderId(Long orderId);

    List<OrderDetail> findByOrderId(Long orderId);
}
