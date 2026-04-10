package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.req.auth.LoginReq;
import com.backend.kamnywesoliqourbackend.dto.req.auth.RegisterReq;
import com.backend.kamnywesoliqourbackend.dto.req.auth.VerifyOtpReq;
import com.backend.kamnywesoliqourbackend.dto.res.auth.AuthRes;
import com.backend.kamnywesoliqourbackend.dto.res.auth.MessageRes;
import com.backend.kamnywesoliqourbackend.service.impl.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageRes> register(@RequestBody RegisterReq request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<MessageRes> login(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthRes> verifyOtp(@RequestBody VerifyOtpReq request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }
}
