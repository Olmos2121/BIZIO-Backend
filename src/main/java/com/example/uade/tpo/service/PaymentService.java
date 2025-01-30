//package com.example.uade.tpo.service;
//
//import com.example.uade.tpo.dtos.request.CardRequestDto;
//import com.example.uade.tpo.entity.Order;
//import com.example.uade.tpo.repository.IOrderRepository;
//import com.example.uade.tpo.repository.IUserRepository;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.model.PaymentMethod;
//import com.stripe.model.Token;
//import com.stripe.param.PaymentIntentCreateParams;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class PaymentService {
//
//    @Autowired
//    private IOrderRepository orderRepository;
//
//    public PaymentIntent createPaymentIntent(Long orderId, CardRequestDto cardDto) throws StripeException {
//        Optional<Order> order = orderRepository.findById(orderId);
//        if (order.isEmpty()){
//            throw new IllegalArgumentException("Order not found");
//        }
//        String currency = "usd";
//        PaymentIntentCreateParams Params =
//                PaymentIntentCreateParams.builder()
//                        .setPaymentMethod(createPaymentMethod(cardDto))
//                        .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
//                        .setConfirm(true)
//                        .setCustomer(order.get().getUser().getUsername())
//                        .setDescription("Order " + order.get().getId())
//                        .setCurrency(currency)
//                        .setAmount((long) (order.get().getTotalAmount() * 100))
//                        .build();
//
//        return PaymentIntent.create(Params);
//    }
//
//    private String createPaymentMethod(CardRequestDto cardDto) throws StripeException {
//        Map<String, Object> card = new HashMap<>();
//        card.put("number", cardDto.getCardNumber());
//        card.put("exp_month", cardDto.getExpirationDate().getMonthValue());
//        card.put("exp_year", cardDto.getExpirationDate().getYear());
//        card.put("cvc", cardDto.getCvv());
//        Map<String, Object> tokenParams = new HashMap<>();
//        tokenParams.put("card", card);
//
//        Token token = Token.create(tokenParams);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("type", "card");
//        params.put("card", Collections.singletonMap("token", token.getId()));
//
//        PaymentMethod paymentMethod = PaymentMethod.create(params);
//        return paymentMethod.getId();
//    }
//}
