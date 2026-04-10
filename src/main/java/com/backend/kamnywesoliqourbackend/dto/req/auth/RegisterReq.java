package com.backend.kamnywesoliqourbackend.dto.req.auth;

public record RegisterReq(
        String name,
        String phone,
        String email,
        String branchName,
        String role,
        String password
) {
}
