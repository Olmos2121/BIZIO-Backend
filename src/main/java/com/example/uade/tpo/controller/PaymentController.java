//package com.example.uade.tpo.controller;
//
//import com.example.uade.tpo.dtos.request.CardRequestDto;
//import com.example.uade.tpo.service.PaymentService;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentController {
//    @Autowired
//    private PaymentService paymentService;
//
//    @PostMapping("/create")//Create payment
//    public ResponseEntity<String> createPayment(@RequestParam Long orderId,
//                                                @RequestBody CardRequestDto cardDto) {
//        try {
//            PaymentIntent paymentIntent = paymentService.createPaymentIntent(orderId, cardDto);
//            return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.CREATED);
//        } catch (StripeException e) {
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//    }
//
//    @PostMapping("/checkout-session")
//    public ResponseEntity<String> checkoutSession() {
//        try {
//            String YOUR_DOMAIN = "http://localhost:4242";
//            SessionCreateParams params =
//                    SessionCreateParams.builder()
//                            .setMode(SessionCreateParams.Mode.PAYMENT)
//                            .setSuccessUrl(YOUR_DOMAIN + "?success=true")
//                            .setCancelUrl(YOUR_DOMAIN + "?canceled=true")
//                            .addLineItem(
//                                    SessionCreateParams.LineItem.builder()
//                                            .setQuantity(1L)
//                                            .setPriceData(
//                                                    SessionCreateParams.LineItem.PriceData.builder()
//                                                            .setCurrency("usd")
//                                                            .setUnitAmount(2000L)
//                                                            .setProductData(
//                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                            .setName("T-shirt")
//                                                                            .build())
//                                                            .build())
//                                            .setPrice("{{PRICE_ID}}")
//                                            .build())
//                            .build();
//            Session session = Session.create(params);
//
//            return new ResponseEntity<>(session.getUrl(), HttpStatus.FOUND);
//        } catch (StripeException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}