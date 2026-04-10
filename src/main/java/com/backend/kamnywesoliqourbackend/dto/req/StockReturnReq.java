package com.backend.kamnywesoliqourbackend.dto.req;

import java.util.UUID;

public record StockReturnReq(
        UUID branchId,
        UUID drinkId,
        UUID raisedBy,
        Integer quantity,
        String reason
) {}