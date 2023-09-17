package com.nullnumber1.lab1.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_document")
public class PaymentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    public void setId(Long id) {
        this.id = id;
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
