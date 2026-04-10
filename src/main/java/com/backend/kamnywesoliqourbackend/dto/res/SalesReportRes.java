package com.backend.kamnywesoliqourbackend.dto.res;

import java.math.BigDecimal;

public record SalesReportRes(
        String branchName,
        Long totalOrders,
        BigDecimal totalRevenue
) {}