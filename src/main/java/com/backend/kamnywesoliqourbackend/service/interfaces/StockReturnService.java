package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.dto.req.StockReturnReq;
import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;

import java.util.List;
import java.util.UUID;

public interface StockReturnService {
    StockReturn processStockReturn(StockReturnReq req);
    List<StockReturn> getStockReturnsByBranch(UUID branchId);
    List<StockReturn> getAllStockReturns();
    StockReturn updateStockReturnStatus(UUID id, ReturnStatus status, String hqResponse) ;

}
