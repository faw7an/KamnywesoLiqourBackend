package com.backend.kamnywesoliqourbackend.dto.res;

public record InventoryReportRes(
        String drinkName,
        Integer currentQuantity,
        Integer minThreshold,
        String status // e.g., "LOW STOCK" or "OK"
) {}