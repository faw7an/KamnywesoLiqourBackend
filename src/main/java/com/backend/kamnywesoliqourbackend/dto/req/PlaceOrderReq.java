package com.backend.kamnywesoliqourbackend.dto.req;


import java.util.List;
import java.util.UUID;

public record PlaceOrderReq(
        UUID branchId, UUID customerId, String customerName , String customerPhoneNumber,UUID loyaltyCardId,UUID staffId, List<OrderItemReq> items
) {
}
