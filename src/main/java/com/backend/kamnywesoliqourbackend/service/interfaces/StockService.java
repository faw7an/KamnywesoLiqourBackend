package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Stock;

import java.util.List;
import java.util.UUID;

public interface StockService {
    List<Stock> getAllStocks();
    Stock createStock(Stock stock);
    List<Stock> getStockByBranch(UUID branchId);
    Stock restockBranch(UUID branchId, Integer quantity, UUID drinkId);
    Stock getStockByDrinkAndBranch(UUID drinkId, UUID branchId);

//    void deleteStock(Long id);
}
