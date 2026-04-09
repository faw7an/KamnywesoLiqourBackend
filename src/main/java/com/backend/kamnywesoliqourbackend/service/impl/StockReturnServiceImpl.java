package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
import com.backend.kamnywesoliqourbackend.repository.StockReturnRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockReturnService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockReturnServiceImpl implements StockReturnService {
    private final StockReturnRepository stockReturnRepository;

    public StockReturnServiceImpl(StockReturnRepository stockReturnRepository){
        this.stockReturnRepository = stockReturnRepository;
    }

    @Override
    public List<StockReturn> getAllStockReturns() {
        return stockReturnRepository.findAll();
    }
    @Override
    public StockReturn processStockReturn(UUID branchId, StockReturn stockReturn) {
        stockReturn.setStatus(ReturnStatus.PENDING_REVIEW);
        return stockReturnRepository.save(stockReturn);
    }

    @Override
    public List<StockReturn> getStockReturnsByBranch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }
        return stockReturnRepository.findByBranch_Id(branchId);
    }


    @Override
    public StockReturn updateStockReturn(UUID stockReturnId, StockReturn stockReturn) {
        if(stockReturnId == null || stockReturn == null) {
            throw new RuntimeException("Stock return id and stock return are required");
        }
        StockReturn existingStockReturn = stockReturnRepository.findById(stockReturnId).orElseThrow(()-> new RuntimeException("Stock return not found"));
        existingStockReturn.setStatus(stockReturn.getStatus());
        return stockReturnRepository.save(existingStockReturn);
    }
}
