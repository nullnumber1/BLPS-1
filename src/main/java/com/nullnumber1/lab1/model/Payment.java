package com.nullnumber1.lab1.model;

import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private PaymentType paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private OKTMO oktmo;

    @OneToOne(cascade = CascadeType.ALL)
    private Payer payer;

    private Boolean forSelf;

    @OneToOne(cascade = CascadeType.ALL)
    private Payee payee;

    @OneToOne(cascade = CascadeType.ALL)
    private PaymentDocument paymentDocument;

    private String status;

    public Long getId() {
        return id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public OKTMO getOktmo() {
        return oktmo;
    }

    public void setOktmo(OKTMO oktmo) {
        this.oktmo = oktmo;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public Boolean getForSelf() {
        return forSelf;
    }

    public void setForSelf(Boolean forSelf) {
        this.forSelf = forSelf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
