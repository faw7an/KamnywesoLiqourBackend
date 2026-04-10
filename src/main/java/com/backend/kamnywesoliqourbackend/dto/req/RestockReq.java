package com.backend.kamnywesoliqourbackend.dto.req;

import java.util.UUID;

public record RestockReq(
        UUID branchId,
        UUID drinkId,
        Integer quantity
) {}