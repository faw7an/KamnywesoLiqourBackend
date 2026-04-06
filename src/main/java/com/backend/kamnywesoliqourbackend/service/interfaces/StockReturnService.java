package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.StockReturn;

import java.util.List;
import java.util.UUID;

public interface StockReturnService {
    StockReturn processStockReturn(UUID branchId,StockReturn stockReturn);
    List<StockReturn> getStockReturnsByBranch(UUID branchId);
    List<StockReturn> getAllStockReturns();
    StockReturn updateStockReturn(UUID stockReturnId,StockReturn stockReturn);

}
