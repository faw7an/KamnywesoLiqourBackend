package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import com.backend.kamnywesoliqourbackend.repository.OrderRepository;
import com.backend.kamnywesoliqourbackend.repository.ReportRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final OrderRepository orderRepository;

    public ReportServiceImpl(ReportRepository reportRepository, OrderRepository orderRepository){
        this.reportRepository = reportRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getSalesReport(UUID branchId, LocalDate dateFrom, LocalDate dateTo) {
        return orderRepository.findByBranch_IdAndCreatedAtBetween(branchId, dateFrom.atStartOfDay(), dateTo.plusDays(1).atStartOfDay());
    }

    @Override
    public BigDecimal getProfitLoss(UUID branchId, LocalDate dateFrom, LocalDate dateTo) {
        return null;
    }

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }
}
