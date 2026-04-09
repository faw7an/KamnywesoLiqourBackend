package com.backend.kamnywesoliqourbackend.dto.res;

import java.math.BigDecimal;
import java.util.UUID;

public record DrinkRes(
        UUID id, String name, String brand, String category, BigDecimal price, String imageUrl
) {
}
