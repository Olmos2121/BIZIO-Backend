package com.example.uade.tpo.repository;

import com.example.uade.tpo.entity.Discount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDiscountRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END FROM Discount d WHERE d.code = :code")

    boolean existsByCode(String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM Discount d WHERE d.code = :code")
    void deleteByCode(String code);

    Optional<Discount> findByCode(String code);
}
