package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.LoyaltyTier;

import java.util.UUID;

public record LoyaltyCustomerRes(
        UUID id, String name, String phone, String cardId, LoyaltyTier tier, Integer pointBalance
) {
}
