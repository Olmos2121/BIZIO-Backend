package com.example.uade.tpo.repository;

import com.example.uade.tpo.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellerRepository extends JpaRepository<Seller, Long> {
}
