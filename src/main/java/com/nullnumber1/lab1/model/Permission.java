package com.nullnumber1.lab1.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_APPROVE("admin:approve"),

    ;

    @Getter
    private final String permission;
}