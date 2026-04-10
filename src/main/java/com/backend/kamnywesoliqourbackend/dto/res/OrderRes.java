package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderRes(
        UUID id,
        String customerName,
        String customerPhone,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        String branchName,
        List<OrderItemRes> items
) {
}
