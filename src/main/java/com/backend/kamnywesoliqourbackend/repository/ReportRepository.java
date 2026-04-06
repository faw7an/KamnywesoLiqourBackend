package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Order> getSalesReport(UUID branchId, LocalDate dateFrom, LocalDate dateTo);
}
