package com.example.uade.tpo.repository;

import com.example.uade.tpo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long id);

    Optional<CartItem> findByCartIdAndProductId(Long id, Long productId);

    void deleteByCartIdAndProductId(Long id, Long productId);
}
