package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.dto.AuthResponse;
import com.nullnumber1.lab1.model.Role;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.repository.RoleRepository;
import com.nullnumber1.lab1.repository.UserRepository;
import com.nullnumber1.lab1.service.JwtTokenProvider;
import com.nullnumber1.lab1.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Registration management")
public class RegistrationController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/api/register")
    @Operation(description = "Register user", responses = {
            @ApiResponse(responseCode = "200", description = "User was successfully registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role defaultRole = roleRepository.findByName("client");

        List<Role> userRoles = new ArrayList<>(Collections.singletonList(defaultRole));

        user.setRoles(userRoles);

        userRepository.save(user);

        String jwt = jwtTokenProvider.createToken(user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return new AuthResponse(jwt, refreshToken);
    }

}
