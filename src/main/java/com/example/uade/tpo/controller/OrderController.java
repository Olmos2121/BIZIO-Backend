package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.response.OrderResponseDto;
import com.example.uade.tpo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}") //Get order by id
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = orderService.getOrderById(orderId);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}") //Get orders by user id
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrderByUserId(userId);
        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping ("/{cartId}")//Create order from cart
    public ResponseEntity<OrderResponseDto> createOrderFromCart(@PathVariable Long cartId) {
        OrderResponseDto newOrder = orderService.createOrderFromCart(cartId);
        if(newOrder == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{orderId}") //Delete order
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        Boolean deleted = orderService.deleteOrder(orderId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{orderId}/discount/{code}") //Apply discount to order
    public ResponseEntity<OrderResponseDto> applyDiscountToOrder(@PathVariable Long orderId, @PathVariable String code) {
        OrderResponseDto order = orderService.applyDiscountToOrder(orderId, code);
        if(order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
