package com.backend.kamnywesoliqourbackend.dto.req;

import com.backend.kamnywesoliqourbackend.enums.Role;

import java.util.UUID;

public record RegisterReq(
        String name, String phoneNumber, String email, String password, Role role, UUID branch
        ) {}
