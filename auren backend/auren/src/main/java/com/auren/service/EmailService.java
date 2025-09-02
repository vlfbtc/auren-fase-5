package com.auren.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class EmailService {
    // Mock: apenas loga o PIN. Em produção, integre com SMTP/Sendgrid etc.
    public void sendSignupPin(String email, String pin) {
        log.info("[MOCK EMAIL] Enviando PIN {} para {}", pin, email);
    }
}
