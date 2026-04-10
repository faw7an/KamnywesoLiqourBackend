package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockReturnRes(
        UUID id,
        String branchName,
        String drinkName,
        Integer quantity,
        String reason,
        ReturnStatus status,
        String hqResponse,
        LocalDateTime createdAt
) {}