package com.example.uade.tpo.entity.paymentMethod;

public interface ICardMethod extends IPaymentMethod{
    void setCardNumber(Long cardNumber);
    void setExpirationDate(String expirationDate);
    void setSecurityCode(Integer securityCode);
    void setNameOnCard(String nameOnCard);
}
