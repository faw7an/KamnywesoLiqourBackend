package com.backend.kamnywesoliqourbackend.dto.res;

import java.math.BigDecimal;
import java.util.UUID;

public record StockRes(
        UUID id,
        UUID drinkId,
        String name,          // Changed from drinkName to match frontend "name"
        String brand,         // Added to match frontend
        String branch,        // Added to match frontend
        Integer stock,        // Changed from quantity to match frontend "stock"
        Integer min,          // Changed from minThreshold to match frontend "min"
        String status,        // Added (e.g. "OK", "Low", "Out of Stock")
        String restocked,     // Changed from LocalDateTime lastRestock to formatted String "22 Oct"
        BigDecimal price,     // Added to match PlaceOrder UI
        String image          // Added to match PlaceOrder UI
) {
}
