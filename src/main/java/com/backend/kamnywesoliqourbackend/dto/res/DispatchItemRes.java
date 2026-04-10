package com.backend.kamnywesoliqourbackend.dto.res;

import java.util.UUID;

public record DispatchItemRes(
        UUID id,
        UUID drinkId,
        String drinkName,
        String brand,
        Integer quantity
) {}