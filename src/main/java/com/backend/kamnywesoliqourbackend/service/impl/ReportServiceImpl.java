package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.OrderItem;
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
        // We convert LocalDate to LocalDateTime (start of day to end of day)
        return orderRepository.findByBranch_IdAndCreatedAtBetween(
                branchId,
                dateFrom.atStartOfDay(),
                dateTo.plusDays(1).atStartOfDay()
        );
    }

    @Override
    public BigDecimal getProfitLoss(UUID branchId, LocalDate dateFrom, LocalDate dateTo) {
        List<Order> orders = getSalesReport(branchId, dateFrom, dateTo);

        BigDecimal totalProfit = BigDecimal.ZERO;

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                // Profit = (Selling Price - Cost Price) * Quantity
                BigDecimal sellingPrice = item.getUnitPrice();
                BigDecimal costPrice = item.getDrink().getCostPrice();

                BigDecimal unitProfit = sellingPrice.subtract(costPrice);
                BigDecimal lineProfit = unitProfit.multiply(BigDecimal.valueOf(item.getQuantity()));

                totalProfit = totalProfit.add(lineProfit);
            }
        }
        return totalProfit;
    }

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }
}