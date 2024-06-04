package com.example.uade.tpo.entity.paymentMethod;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MercadoPago implements IOnlineMethod {
    String email;
    String password;

    @Override
    public Boolean pay(double amount) {
        if (email != null && password != null) {
            this.email = null;
            this.password = null;
            return true;
        }
        return false;
    }


}
