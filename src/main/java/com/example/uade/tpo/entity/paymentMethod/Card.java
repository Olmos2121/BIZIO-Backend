package com.example.uade.tpo.entity.paymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card implements PaymentMethod {
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private int cvv;

    @Override
    public void pay(double amount) {
        System.out.println("Paying " + amount + " with card " + cardNumber);
    }
}
