package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.Role;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;

import java.util.UUID;

public record UserRes(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        UserStatus status,
        String branchName,
        UUID branchId
) {}
