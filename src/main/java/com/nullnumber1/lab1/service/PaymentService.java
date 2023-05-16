package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.model.*;
import com.nullnumber1.lab1.repository.InnRepository;
import com.nullnumber1.lab1.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InnRepository innRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, InnRepository innRepository) {
        this.paymentRepository = paymentRepository;
        this.innRepository = innRepository;
    }

    public Long createPayment() {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.INITIATED.name());
        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment.getId();
    }

    public PaymentStatus getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return PaymentStatus.valueOf(payment.getStatus());
    }

    public void updatePaymentType(Long paymentId, String type, Double amount) {
        // retrieve payment from DB
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // update paymentType and save it
        PaymentType paymentType = new PaymentType();
        paymentType.setType(type);
        paymentType.setAmount(new BigDecimal(amount));
        payment.setPaymentType(paymentType);
        payment.setStatus(PaymentStatus.FILLING_TYPE_AMOUNT.name());
        paymentRepository.save(payment);
    }

    public void updateOKTMO(Long paymentId, String code) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        OKTMO oktmo = new OKTMO();
        oktmo.setCode(code);
        payment.setOktmo(oktmo);
        payment.setStatus(PaymentStatus.FILLING_OKTMO.name());
        paymentRepository.save(payment);
    }

    public PaymentDocument updatePayer(Long paymentId, String name, Long INN, Boolean forSelf) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        Payer payer = new Payer();
        payer.setName(name);
        payer.setINN(INN);
        payment.setPayer(payer);
        payment.setForSelf(forSelf);
        payment.setStatus(PaymentStatus.FILLING_PAYER.name());
        paymentRepository.save(payment);

        if (forSelf) {
            return checkInnAndGenerateDocument(payment);
        } else {
            return null;
        }
    }

    public PaymentDocument updatePayee(Long paymentId, String name, Long INN) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        Payee payee = new Payee();
        payee.setName(name);
        payee.setINN(INN);
        payment.setPayee(payee);
        payment.setStatus(PaymentStatus.FILLING_PAYEE.name());
        paymentRepository.save(payment);

        return checkInnAndGenerateDocument(payment);
    }

    private PaymentDocument checkInnAndGenerateDocument(Payment payment) {
        if (payment.getForSelf()) {
            if (innRepository.existsById(payment.getPayer().getINN())) {
                return generatePaymentDocument(payment);
            }
        } else {
            if (innRepository.existsById(payment.getPayee().getINN())) {
                return generatePaymentDocument(payment);
            }
        }
        return null;
    }

    private PaymentDocument generatePaymentDocument(Payment payment) {
        PaymentDocument paymentDocument = new PaymentDocument();
        paymentDocument.setPayerInn(payment.getPayer().getINN());
        if (!payment.getForSelf()) {
            paymentDocument.setPayeeInn(payment.getPayee().getINN());
        }
        paymentDocument.setOrganizationOktmo(payment.getOktmo().getCode());
        paymentDocument.setAmount(payment.getPaymentType().getAmount());
        paymentDocument.setPaymentType(payment.getPaymentType().getType());
        paymentDocument.setDateOfPayment(LocalDate.now());
        return paymentDocument;
    }

    public boolean processPayment(Long paymentId, String paymentMethod) {
        // Simulate processing time
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        Random random = new Random();
        boolean paymentProcessed = random.nextBoolean();

        if (paymentProcessed) {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setStatus(PaymentStatus.PROCESSED.name());
            paymentRepository.save(payment);
            log.info("Payment processed successfully using " + paymentMethod);
        } else {
            log.warn("Payment failed using " + paymentMethod);
        }

        return paymentProcessed;
    }
}