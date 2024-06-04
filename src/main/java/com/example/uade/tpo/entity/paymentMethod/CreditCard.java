package com.example.uade.tpo.entity.paymentMethod;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreditCard implements ICardMethod {

    Long cardNumber;
    String expirationDate;
    Integer securityCode;
    String nameOnCard;

    @Override
    public Boolean pay(double amount) {
        if (cardNumber != null && expirationDate != null && securityCode != null && nameOnCard != null) {
            this.cardNumber = null;
            this.expirationDate = null;
            this.securityCode = null;
            this.nameOnCard = null;
            return true;
        }
        return false;
    }
}

