package com.nullnumber1.lab1.model;

import javax.persistence.*;

@Entity
@Table(name = "oktmo")
public class OKTMO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
