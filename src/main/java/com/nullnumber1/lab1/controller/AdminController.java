package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.model.PaymentDocument;
import com.nullnumber1.lab1.model.PaymentStatus;
import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin management")
public class AdminController {

    private final PaymentService paymentService;

    @Autowired
    public AdminController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/reviewPayment/{paymentId}")
    @Transactional
    @Operation(description = "Review payment", responses = {
            @ApiResponse(responseCode = "200", description = "Payment was successfully reviewed"),
            @ApiResponse(responseCode = "400", description = "Payment is not processed yet"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> reviewPayment(@PathVariable Long paymentId, @RequestParam Boolean decision) {
        PaymentStatus status = paymentService.getPaymentStatus(paymentId);
        if (status.equals(PaymentStatus.PROCESSED)) {
            if (decision) {
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.APPROVED);
                return new ResponseEntity<>("Payment Approved", HttpStatus.OK);
            } else {
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
                return new ResponseEntity<>("Payment Failed", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Payment is not processed yet.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paymentDocument/{paymentId}")
    @Operation(description = "Get payment document", responses = {
            @ApiResponse(responseCode = "200", description = "Payment document was successfully received"),
            @ApiResponse(responseCode = "400", description = "Payment document was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentDocument> getPaymentDocument(@PathVariable Long paymentId) {
        try {
            PaymentDocument paymentDocument = paymentService.getPaymentDocument(paymentId);
            return new ResponseEntity<>(paymentDocument, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
