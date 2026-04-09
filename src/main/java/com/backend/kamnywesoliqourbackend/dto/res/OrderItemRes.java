package com.backend.kamnywesoliqourbackend.dto.res;

import java.math.BigDecimal;


public record OrderItemRes(
        String drinkName, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal
) {
}
