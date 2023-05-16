package com.nullnumber1.lab1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Inn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long inn;

    public Long getInn() {
        return inn;
    }

    public void setInn(Long inn) {
        this.inn = inn;
    }
}
