package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.exception.InvalidTokenException;
import com.nullnumber1.lab1.model.RefreshToken;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.repository.RefreshTokenRepository;
import com.nullnumber1.lab1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;


    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String createRefreshToken(String username) {
        User user = userRepository.findByUsername(username);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public String validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken == null) {
            throw new InvalidTokenException();
        }
        refreshTokenRepository.delete(refreshToken);
        return refreshToken.getUser().getUsername();
    }
}