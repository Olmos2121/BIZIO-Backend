package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.CardRequestDto;
import com.example.uade.tpo.dtos.request.MPRequestDto;
import com.example.uade.tpo.dtos.response.PaymentResponseDto;
import com.example.uade.tpo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process/order/{orderId}")//Process payment
    public ResponseEntity<PaymentResponseDto> processPayment(@PathVariable Long orderId) {
        PaymentResponseDto payment = paymentService.processPayment(orderId);
        if (payment == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PutMapping("/confirmCard/{paymentId}")//Confirm payment with card
    public ResponseEntity<PaymentResponseDto> confirmCardPayment
            (@PathVariable Long paymentId, @RequestBody CardRequestDto cardPaymentMethod) {
        PaymentResponseDto paymentResponseDto = paymentService.confirmCardPayment(paymentId, cardPaymentMethod);
        if (paymentResponseDto == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PutMapping("/confirmMP/{paymentId}")//Confirm payment with MercadoPago
    public ResponseEntity<PaymentResponseDto> confirmMPPayment
            (@PathVariable Long paymentId, @RequestBody MPRequestDto MercadoPagoPaymentMethod) {
        PaymentResponseDto paymentResponseDto = paymentService.confirmMPPayment(paymentId, MercadoPagoPaymentMethod);
        if (paymentResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @PostMapping("/cancel/{paymentId}")//Cancel payment
    public ResponseEntity<PaymentResponseDto> cancelPayment(@PathVariable Long paymentId) {
        PaymentResponseDto paymentResponseDto = paymentService.cancelPayment(paymentId);
        if (paymentResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{paymentId}")//Get payment by id
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long paymentId) {
        Optional<PaymentResponseDto> payment = paymentService.getPaymentById(paymentId);
        return payment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/history/{userId}")//Get payment history by user id
    public ResponseEntity<List<PaymentResponseDto>> getPaymentHistory(@PathVariable Long userId) {
        Optional<List<PaymentResponseDto>> paymentHistory = Optional.ofNullable
                (paymentService.getPaymentHistory(userId));
        return paymentHistory.map(paymentResponseDtos -> new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}