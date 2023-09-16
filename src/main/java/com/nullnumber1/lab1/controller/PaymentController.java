package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.model.PaymentStatus;
import com.nullnumber1.lab1.model.UserPrincipal;
import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment management")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(description = "Create payment", responses = {
            @ApiResponse(responseCode = "200", description = "Payment was successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> initPayment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getUser().getId();

        Long paymentId = paymentService.createPayment(userId);
        return ResponseEntity.ok(paymentId);
    }


    @GetMapping("/{paymentId}")
    @Operation(description = "Get payment status", responses = {
            @ApiResponse(responseCode = "200", description = "Payment status was successfully received"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentStatus> getPaymentStatus(
            @PathVariable(value = "paymentId") Long paymentId
    ) {
        PaymentStatus status = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }
}
