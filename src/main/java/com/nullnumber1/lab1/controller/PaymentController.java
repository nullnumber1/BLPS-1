package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.model.PaymentStatus;
import com.nullnumber1.lab1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Long> initPayment() {
        Long paymentId = paymentService.createPayment();
        return ResponseEntity.ok(paymentId);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentStatus> getPaymentStatus(
            @PathVariable(value = "paymentId") Long paymentId
    ) {
        PaymentStatus status = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }
}