package com.backend.kamnywesoliqourbackend.dto.res;

import com.backend.kamnywesoliqourbackend.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderRes(
        UUID orderId,              // Keep UUID for references
        String id,                 // Map to formatted ORD-XXXX string or just UUID string for UI
        String customer,           // frontend expects "customer"
        String items,              // frontend expects string summary "Tusker x12, Guinness x4"
        String total,              // formatted String "4,800"
        String status,             // String value "Received"
        String time,               // formatted string "24 Oct, 08:15"
        String staff,              // cashier/staff name
        Integer loyalty,           // computed loyalty points
        String branchName,         
        List<OrderItemRes> orderItems // keeping the detailed array just in case it's needed
) {
}
