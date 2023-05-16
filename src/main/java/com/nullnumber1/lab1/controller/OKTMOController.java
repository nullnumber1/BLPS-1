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
public class OKTMOController {

    private PaymentService paymentService;

    @Autowired
    public OKTMOController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/oktmo")
    public ResponseEntity<Void> fillOKTMO(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "code") String code
    ) {
        paymentService.updateOKTMO(paymentId, code);
        return ResponseEntity.ok().build();
    }
}