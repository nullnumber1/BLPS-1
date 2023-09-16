package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.dto.AuthRequest;
import com.nullnumber1.lab1.dto.AuthResponse;
import com.nullnumber1.lab1.dto.RefreshRequest;
import com.nullnumber1.lab1.service.CustomUserDetailsService;
import com.nullnumber1.lab1.service.JwtTokenProvider;
import com.nullnumber1.lab1.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication management")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService, CustomUserDetailsService userDetailsService) { // Update constructor
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping("/api/authenticate")
    @Operation(description = "Authenticate user", responses = {
            @ApiResponse(responseCode = "200", description = "User was successfully authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication.getName());
        String refreshToken = refreshTokenService.createRefreshToken(authentication.getName());
        return new AuthResponse(jwt, refreshToken);
    }

    @PostMapping("/api/refresh")
    @Operation(description = "Refresh token", responses = {
            @ApiResponse(responseCode = "200", description = "Token was successfully refreshed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String refresh(@RequestBody RefreshRequest refreshRequest) {
        String username = refreshTokenService.validateRefreshToken(refreshRequest.refreshToken());
        return jwtTokenProvider.createToken(username);
    }
}
