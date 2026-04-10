package com.backend.kamnywesoliqourbackend.dto.req;

import java.util.List;
import java.util.UUID;

public record DispatchReq(
        UUID branchId,
        String driverName,
        String vehiclePlate,
        String notes,
        List<DispatchItemReq> items
) {
}
