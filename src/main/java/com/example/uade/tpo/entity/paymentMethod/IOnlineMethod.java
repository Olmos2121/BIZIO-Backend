package com.example.uade.tpo.entity.paymentMethod;

public interface IOnlineMethod extends IPaymentMethod{
    void setEmail(String mail);
    void setPassword(String password);
}
