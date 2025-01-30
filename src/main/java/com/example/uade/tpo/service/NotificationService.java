package com.example.uade.tpo.service;

import com.example.uade.tpo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService;

    public void sendProblemMail(String problematica, String descripcion, String fullName) {
        emailService.sendEmail("trialmatch2024@gmail.com", "Nombre: "+ fullName +"- Problema: " + problematica, descripcion);
    }
}
