package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment type management")
public class PaymentTypeController {

    private PaymentService paymentService;

    @Autowired
    public PaymentTypeController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/type")
    @Operation(description = "Fill the payment type", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment type was successfully filled"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> fillPaymentType(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "amount") Double amount
    ) {
        paymentService.updatePaymentType(paymentId, type, amount);
        return ResponseEntity.ok().build();
    }
}