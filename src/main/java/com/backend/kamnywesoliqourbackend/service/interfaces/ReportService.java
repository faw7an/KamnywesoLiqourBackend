package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import com.backend.kamnywesoliqourbackend.entity.Stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReportService {
    List<Order> getSalesReport(UUID branchId, LocalDate dateFrom, LocalDate dateTo);
    BigDecimal getProfitLoss(UUID branchId, LocalDate dateFrom, LocalDate dateTo);
    Report saveReport(Report report);
}
