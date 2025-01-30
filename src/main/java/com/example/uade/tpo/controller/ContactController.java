package com.example.uade.tpo.controller;

import com.example.uade.tpo.entity.ContactForm;
import com.example.uade.tpo.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final FileStorageService fileStorageService;

    public ContactController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<?> submitForm(@Valid @RequestBody ContactForm contactForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        String[] fotos = contactForm.getFotos();
        if (fotos != null) {
            for (String fotoUrl : fotos) {
                fileStorageService.storeFileFromUrl(fotoUrl);
            }
        }

        return ResponseEntity.ok("El problema ha sido registrado correctamente");
    }
}
