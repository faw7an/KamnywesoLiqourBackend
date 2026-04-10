package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.res.OrderRes;
import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import com.backend.kamnywesoliqourbackend.service.interfaces.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Endpoint for the list of orders
    @GetMapping("/sales/{branchId}")
    public ResponseEntity<List<Order>> getSalesReport(
            @PathVariable UUID branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(reportService.getSalesReport(branchId, from, to));
    }

    // Endpoint for the total profit number
    @GetMapping("/profit/{branchId}")
    public ResponseEntity<BigDecimal> getProfit(
            @PathVariable UUID branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(reportService.getProfitLoss(branchId, from, to));
    }

    // Endpoint to log/save a generated report
    @PostMapping
    public ResponseEntity<Report> saveReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportService.saveReport(report));
    }
}