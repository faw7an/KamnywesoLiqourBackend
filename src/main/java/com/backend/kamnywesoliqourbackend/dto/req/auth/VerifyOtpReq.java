package com.backend.kamnywesoliqourbackend.dto.req.auth;

public record VerifyOtpReq(String email, String otpCode) {
}
