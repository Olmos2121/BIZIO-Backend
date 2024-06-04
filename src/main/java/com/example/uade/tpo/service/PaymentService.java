package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.CardRequestDto;
import com.example.uade.tpo.dtos.request.MPRequestDto;
import com.example.uade.tpo.dtos.response.PaymentResponseDto;
import com.example.uade.tpo.entity.Order;
import com.example.uade.tpo.entity.OrderDetail;
import com.example.uade.tpo.entity.Payment;
import com.example.uade.tpo.entity.Product;
import com.example.uade.tpo.entity.paymentMethod.*;
import com.example.uade.tpo.repository.IOrderDetailRepository;
import com.example.uade.tpo.repository.IOrderRepository;
import com.example.uade.tpo.repository.IPaymentRepository;
import com.example.uade.tpo.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    public PaymentResponseDto processPayment(Long orderId) {
        Payment payment = new Payment();
        List<Long> paymentIds = paymentRepository.getAllPaymentIds();
        if(paymentIds.contains(orderId)){
            return null;
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || "PAID".equals(order.getStatus()))   {
            return null;
        }

        for(OrderDetail orderDetail : orderDetailRepository.findByOrderId(orderId)){
            Product product = productRepository.findById(orderDetail.getProductId()).orElse(null);
            if (product == null || product.getStock() < orderDetail.getQuantity()) {
                return null;
            }
            product.setStock(product.getStock() - orderDetail.getQuantity());
            productRepository.save(product);
        }
        payment.setOrderId(orderId);
        payment.setPaymentAmount(orderRepository.findById(orderId).get().getTotalAmount());
        payment.setPaymentStatus("PENDING");
        payment.setPaymentDate(orderRepository.findById(orderId).get().getOrderDate());

        Payment savedPayment = paymentRepository.save(payment);
        return Mapper.convertToPaymentResponseDto(savedPayment);
    }

    public PaymentResponseDto confirmMPPayment(Long paymentId, MPRequestDto mercadoPagoPaymentMethod) {
        MercadoPago mercadoPago = new MercadoPago();
        mercadoPago.setEmail(mercadoPagoPaymentMethod.getEmail());
        mercadoPago.setPassword(mercadoPagoPaymentMethod.getPassword());
        return confirmPayment(paymentId, mercadoPago);
    }

    public PaymentResponseDto confirmCardPayment(Long paymentId, CardRequestDto cardPaymentMethod) {
        CreditCard card = new CreditCard();
        card.setCardNumber(cardPaymentMethod.getCardNumber());
        card.setNameOnCard(cardPaymentMethod.getNameOnCard());
        card.setSecurityCode(cardPaymentMethod.getSecurityCode());
        card.setExpirationDate(cardPaymentMethod.getExpirationDate());
        return confirmPayment(paymentId, card);
    }

    private <T extends IPaymentMethod> PaymentResponseDto confirmPayment(Long paymentId, T paymentMethod) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent() && paymentMethod.pay(optionalPayment.get().getPaymentAmount())) {
            Order order = orderRepository.findById(optionalPayment.get().getOrderId()).orElse(null);
            Payment payment = optionalPayment.get();
            payment.setPaymentStatus("CONFIRMED");
            Payment savedPayment = paymentRepository.save(payment);
            if (order != null) {
                order.setStatus("PAID");
                orderRepository.save(order);
            }
            return Mapper.convertToPaymentResponseDto(savedPayment);
        }
        return null;
    }

    public PaymentResponseDto cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            if(!payment.getPaymentStatus().equals("CONFIRMED")) {
                payment.setPaymentStatus("CANCELLED");
                for(OrderDetail orderDetail : orderDetailRepository.findByOrderId(payment.getOrderId())){
                    Product product = productRepository.findById(orderDetail.getProductId()).orElse(null);
                    if (product != null) {
                        product.setStock(product.getStock() + orderDetail.getQuantity());
                        productRepository.save(product);
                    }
                }
                Payment savedPayment = paymentRepository.save(payment);
                return Mapper.convertToPaymentResponseDto(savedPayment);
            }
        }
        return null;
    }

    public Optional<PaymentResponseDto> getPaymentById(Long paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        return payment.map(Mapper::convertToPaymentResponseDto);
    }

    public List<PaymentResponseDto> getPaymentHistory(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }

        List<Payment> payments = new ArrayList<>();
        for (Order order : orders) {
            List<Payment> orderPayments = paymentRepository.findAllByOrderId(order.getId());
            payments.addAll(orderPayments);
        }

        return payments.stream().map(Mapper::convertToPaymentResponseDto).collect(Collectors.toList());
    }
}
