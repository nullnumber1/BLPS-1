package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.model.PaymentDocument;
import com.nullnumber1.lab1.service.PaymentService;
import com.nullnumber1.lab1.service.PdfGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@Slf4j
public class PayerController {

    private final PaymentService paymentService;
    private final PdfGenerationService pdfGenerationService;

    @Autowired
    public PayerController(PaymentService paymentService, PdfGenerationService pdfGenerationService) {
        this.paymentService = paymentService;
        this.pdfGenerationService = pdfGenerationService;
    }

    @PostMapping("/payer")
    public ResponseEntity<byte[]> updatePayer(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "INN") Long INN,
            @RequestParam(value = "for_self") Boolean forSelf) {

        try {
            PaymentDocument paymentDocument = paymentService.updatePayer(paymentId, name, INN, forSelf);
            if (paymentDocument != null) {
                byte[] pdf = pdfGenerationService.generatePdf(paymentDocument);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("filename", "paymentDocument.pdf");
                return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
