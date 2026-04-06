package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockReturnRepository extends JpaRepository<StockReturn, UUID> {
    List<StockReturn> findByBranch_Id(UUID branchId);
}
