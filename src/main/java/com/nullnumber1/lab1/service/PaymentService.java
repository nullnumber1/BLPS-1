package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.model.*;
import com.nullnumber1.lab1.repository.InnRepository;
import com.nullnumber1.lab1.repository.PaymentRepository;
import com.nullnumber1.lab1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final InnRepository innRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, InnRepository innRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.innRepository = innRepository;
    }

    @Transactional
    public Long createPayment(Integer userId) {
        if (hasIncompletePayments(userId)) {
            return -1L;
        }

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.INITIATED.name());
        payment.setUser(userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User not found")));
        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment.getId();
    }


    private Payment retrievePayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Transactional(readOnly = true)
    public boolean hasIncompletePayments(Integer userId) {
        List<Payment> payments = paymentRepository.findAllByUserId(userId);
        return payments.stream().anyMatch(payment -> !payment.getStatus().equals(PaymentStatus.PROCESSED.name()));
    }

    @Transactional(readOnly = true)
    public PaymentStatus getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return PaymentStatus.valueOf(payment.getStatus());
    }

    @Transactional
    public void updatePaymentStatus(Long paymentId, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(newStatus.name());
        paymentRepository.save(payment);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updatePaymentType(Long paymentId, String type, Double amount) {
        Payment payment = retrievePayment(paymentId);

        PaymentType paymentType = new PaymentType();
        paymentType.setType(type);
        paymentType.setAmount(new BigDecimal(amount));
        payment.setPaymentType(paymentType);
        payment.setStatus(PaymentStatus.FILLING_TYPE_AMOUNT.name());

        paymentRepository.save(payment);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateOKTMO(Long paymentId, String code) {
        Payment payment = retrievePayment(paymentId);

        OKTMO oktmo = new OKTMO();
        oktmo.setCode(code);
        payment.setOktmo(oktmo);
        payment.setStatus(PaymentStatus.FILLING_OKTMO.name());

        paymentRepository.save(payment);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PaymentDocument updatePayer(Long paymentId, String name, Long INN, Boolean forSelf) {
        Payment payment = retrievePayment(paymentId);

        Payer payer = new Payer();
        payer.setName(name);
        payer.setINN(INN);
        payment.setPayer(payer);
        payment.setForSelf(forSelf);
        payment.setStatus(PaymentStatus.FILLING_PAYER.name());

        paymentRepository.save(payment);

        if (forSelf) {
            log.info("Person with ID " + INN + " is paying for himself");
            return checkInnAndGenerateDocument(payment);
        } else {
            return null;
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PaymentDocument updatePayee(Long paymentId, String name, Long INN) {
        Payment payment = retrievePayment(paymentId);

        Payee payee = new Payee();
        payee.setName(name);
        payee.setINN(INN);
        payment.setPayee(payee);
        payment.setStatus(PaymentStatus.FILLING_PAYEE.name());

        paymentRepository.save(payment);

        return checkInnAndGenerateDocument(payment);
    }

    private PaymentDocument checkInnAndGenerateDocument(Payment payment) {
        log.info("Checking INN for payment: " + payment.getId() + " ...");
        if (payment.getForSelf()) {
            if (innRepository.existsById(payment.getPayer().getINN())) {
                log.info("INN" + payment.getPayer().getINN() + " exists in DB");
                return generatePaymentDocument(payment);
            }
        } else {
            if (innRepository.existsById(payment.getPayee().getINN())) {
                log.info("INN" + payment.getPayee().getINN() + " exists in DB");
                return generatePaymentDocument(payment);
            }
        }
        return null;
    }

    private PaymentDocument generatePaymentDocument(Payment payment) {
        PaymentDocument paymentDocument = new PaymentDocument();
        paymentDocument.setId(payment.getId());
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

    @Transactional(readOnly = true)
    public PaymentDocument getPaymentDocument(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        if (PaymentStatus.valueOf(payment.getStatus()) == PaymentStatus.PROCESSED) {
            return generatePaymentDocument(payment);
        } else {
            throw new RuntimeException("Payment document cannot be generated. Payment is not processed yet.");
        }
    }

    @Transactional
    public boolean processPayment(Long paymentId, String paymentMethod) {
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
