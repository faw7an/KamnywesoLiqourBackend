package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DispatchOrderRes(
        UUID id,
        UUID branchId,
        String branchName,
        DispatchStatus status,
        String driverName,
        String vehiclePlate,
        String notes,             // Added
        LocalDateTime dispatchedAt, // Added
        List<DispatchItemRes> items
) {}