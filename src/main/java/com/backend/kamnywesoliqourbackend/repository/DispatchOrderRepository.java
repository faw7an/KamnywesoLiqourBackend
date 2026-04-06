package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DispatchOrderRepository extends JpaRepository<DispatchOrder, UUID> {
    List<DispatchOrder> findByBranchId(UUID branchId);

    DispatchOrder findByIdAndBranch_Id(UUID id, UUID branchId);
}
