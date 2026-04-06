package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.User;

public interface AuthService {
    String login(String email, String password);
    User register(String name, String email, String password, String phone);
    void logout(String token);
    Boolean verifyOtp(String email, String otp);
    String refreshToken(String token);
}

