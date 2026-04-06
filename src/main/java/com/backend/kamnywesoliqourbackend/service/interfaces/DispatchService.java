package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;

import java.util.List;
import java.util.UUID;

public interface DispatchService {
    List<DispatchOrder> getAllDispatches();
    DispatchOrder createDispatch(DispatchOrder dispatchOrder);
    DispatchOrder approveDispatch(UUID id);
    DispatchOrder getDispatch(UUID id);
    DispatchOrder updateDispatchStatus(UUID id, DispatchStatus status);
    List<DispatchOrder> getBranchDispatch(UUID branchId);
    DispatchOrder confirmBranchDispatch(UUID id,UUID branchId);
}
