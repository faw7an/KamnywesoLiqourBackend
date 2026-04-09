package com.backend.kamnywesoliqourbackend.dto.req;

import java.util.UUID;

public record OrderItemReq(
        UUID drinkId, Integer quantity
) {
}
