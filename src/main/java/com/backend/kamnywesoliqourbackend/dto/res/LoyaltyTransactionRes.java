package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoyaltyTransactionRes(
        UUID id,
        Integer pointsEarned,
        Integer pointsRedeemed,
        TransactionType transactionType,
        UUID orderId,
        LocalDateTime createdAt
) {
}
