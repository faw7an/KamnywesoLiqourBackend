package com.backend.kamnywesoliqourbackend.dto.req;

import java.util.UUID;

public record DispatchItemReq(
        UUID id,
        UUID drinkId,
        Integer quantity
) {
}
