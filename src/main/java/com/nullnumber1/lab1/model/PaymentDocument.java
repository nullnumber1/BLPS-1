package com.nullnumber1.lab1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PaymentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long payerInn;

    private Long payeeInn;

    private String organizationOktmo;

    private String paymentType;

    private BigDecimal amount;

    private LocalDate dateOfPayment;

    public Long getId() {
        return id;
    }

    public Long getPayerInn() {
        return payerInn;
    }

    public void setPayerInn(Long payerInn) {
        this.payerInn = payerInn;
    }

    public Long getPayeeInn() {
        return payeeInn;
    }

    public void setPayeeInn(Long payeeInn) {
        this.payeeInn = payeeInn;
    }

    public String getOrganizationOktmo() {
        return organizationOktmo;
    }

    public void setOrganizationOktmo(String organizationOktmo) {
        this.organizationOktmo = organizationOktmo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
