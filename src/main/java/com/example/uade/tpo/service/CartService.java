package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.CartItemRequestDto;
import com.example.uade.tpo.dtos.response.CartResponseDto;
import com.example.uade.tpo.entity.Cart;
import com.example.uade.tpo.entity.CartItem;
import com.example.uade.tpo.entity.User;
import com.example.uade.tpo.repository.ICartItemRepository;
import com.example.uade.tpo.repository.ICartRepository;
import com.example.uade.tpo.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    public Optional<CartResponseDto> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).map(Mapper::convertToCartResponseDto);
    }

    public CartResponseDto addItemToCart(Long userId, CartItemRequestDto cartItem) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            return null;
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if(cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartRepository.save(cart);
        }

        CartItem existingItem = cartItemRepository.findByCartIdAndProductId
                (cart.getId(), cartItem.getProductId()).orElse(null);

        if(existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductId(cartItem.getProductId());
            newItem.setQuantity(cartItem.getQuantity());
            cartItemRepository.save(newItem);
        }

        return Mapper.convertToCartResponseDto(cart);
    }

    @Transactional
    public Boolean removeItemFromCart(Long userId, Long productId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()) {
            return false;
        }

        Cart cart = cartOptional.get();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        boolean removed = cartItems.removeIf(item -> item.getProductId().equals(productId));

        if(removed){
            if(cartItems.isEmpty()){
                cartRepository.delete(cart);
            }
            return true;
        } else {
            return false;
        }
    }
}
