package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.CartItemRequestDto;
import com.example.uade.tpo.dtos.response.CartResponseDto;
import com.example.uade.tpo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}") //Get cart by user id
    public ResponseEntity<CartResponseDto> getCartByUserId(@PathVariable Long userId) {
        Optional<CartResponseDto> cart = cartService.getCartByUserId(userId);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{userId}/add") //Add item to cart
    public ResponseEntity<CartResponseDto> addItemToCart(@PathVariable Long userId, @RequestBody CartItemRequestDto cartItem) {
        CartResponseDto cart = cartService.addItemToCart(userId, cartItem);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/remove/{productId}") //Remove item from cart
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        Boolean deleted = cartService.removeItemFromCart(userId, productId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
