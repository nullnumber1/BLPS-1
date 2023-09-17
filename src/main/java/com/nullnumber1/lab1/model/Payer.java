package com.nullnumber1.lab1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payer")
public class Payer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private Long INN;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getINN() {
        return INN;
    }

    public void setINN(Long INN) {
        this.INN = INN;
    }
}
