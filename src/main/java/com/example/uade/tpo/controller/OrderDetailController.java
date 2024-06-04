package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.OrderDetailRequestDto;
import com.example.uade.tpo.dtos.response.OrderDetailResponseDto;
import com.example.uade.tpo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/{orderId}") //Get orderDetail by id
    public ResponseEntity<OrderDetailResponseDto> getOrderDetailById(@PathVariable Long orderId) {
        Optional<OrderDetailResponseDto> orderDetail = orderDetailService.getOrderDetailById(orderId);
        return orderDetail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{orderId}") //Update orderDetail
    public ResponseEntity<OrderDetailResponseDto> updateOrderDetail
            (@PathVariable Long orderId, OrderDetailRequestDto orderDetail) {
        OrderDetailResponseDto orderDetailResponseDto = orderDetailService.updateOrderDetail(orderId, orderDetail);
        if(orderDetailResponseDto == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderDetailResponseDto, HttpStatus.OK);
    }
}
