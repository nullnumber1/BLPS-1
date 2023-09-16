package com.nullnumber1.lab1.dto;

public record AuthResponse(
        String jwt,
        String refreshToken
) {
}
