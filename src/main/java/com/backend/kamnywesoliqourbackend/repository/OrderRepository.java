package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByBranch_Id(UUID branchId);
    List<Order> findByBranch_IdAndCreatedAtBetween(UUID branchId, LocalDateTime start, LocalDateTime end);
}
