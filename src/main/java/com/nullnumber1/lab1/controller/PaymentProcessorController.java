package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@Slf4j
public class PaymentProcessorController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentProcessorController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "payment_method") String paymentMethod) {
        try {
            boolean paymentProcessed = paymentService.processPayment(paymentId, paymentMethod);
            if (paymentProcessed) {
                return ResponseEntity.ok("Payment processed successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to process payment");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
        }
    }
}