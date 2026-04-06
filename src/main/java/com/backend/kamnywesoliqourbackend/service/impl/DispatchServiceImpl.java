package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;
import com.backend.kamnywesoliqourbackend.repository.DispatchOrderRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.DispatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DispatchServiceImpl implements DispatchService {
    private final DispatchOrderRepository dispatchOrderRepository;

    public DispatchServiceImpl(DispatchOrderRepository dispatchOrderRepository){
        this.dispatchOrderRepository = dispatchOrderRepository;
    }

    @Override
    public List<DispatchOrder> getAllDispatches() {
        return dispatchOrderRepository.findAll();
    }

    @Override
    public DispatchOrder createDispatch(DispatchOrder dispatchOrder) {
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public DispatchOrder approveDispatch(UUID id) {
        DispatchOrder dispatchOrder = dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(DispatchStatus.APPROVED);
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public DispatchOrder getDispatch(UUID id) {
        if(id == null) {
            throw new RuntimeException("Dispatch id is required");
        }
        return dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
    }

    @Override
    public DispatchOrder updateDispatchStatus(UUID id, DispatchStatus status) {
        if(id == null || status == null) {
            throw new RuntimeException("Dispatch id and status are required");
        }
        DispatchOrder dispatchOrder = dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(status);
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public List<DispatchOrder> getBranchDispatch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }
        return dispatchOrderRepository.findByBranchId(branchId);
    }

    @Override
    public DispatchOrder confirmBranchDispatch(UUID id, UUID branchId) {
        if(id == null || branchId == null) {
            throw new RuntimeException("Dispatch id and branch id are required");
        }
        DispatchOrder dispatchOrder = dispatchOrderRepository.findByIdAndBranch_Id(id, branchId);
        dispatchOrder.setStatus(DispatchStatus.CONFIRMED);
        return dispatchOrder;
    }
}
