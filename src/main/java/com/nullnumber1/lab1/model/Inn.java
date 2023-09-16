package com.nullnumber1.lab1.model;

import javax.persistence.*;

@Entity
@Table(name = "inn_repository")
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
