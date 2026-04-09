package com.backend.kamnywesoliqourbackend.dto.res;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockRes(
        UUID id,
        String drinkName,
        Integer quantity,
        Integer minThreshold,
        LocalDateTime lastRestock
) {
}
