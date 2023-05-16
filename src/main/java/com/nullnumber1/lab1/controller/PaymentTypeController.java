package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@RequiredArgsConstructor
@Slf4j
public class PaymentTypeController {

    private PaymentService paymentService;

    @Autowired
    public PaymentTypeController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/type")
    public ResponseEntity<Void> fillPaymentType(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "amount") Double amount
    ) {
        paymentService.updatePaymentType(paymentId, type, amount);
        return ResponseEntity.ok().build();
    }
}