package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.entity.Drink;
import com.backend.kamnywesoliqourbackend.entity.Stock;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.repository.DrinkRepository;
import com.backend.kamnywesoliqourbackend.repository.StockRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final DrinkRepository drinkRepository;
    private final BranchRepository branchRepository;


    public StockServiceImpl(StockRepository stockRepository, DrinkRepository drinkRepository, BranchRepository branchRepository){
        this.stockRepository = stockRepository;
        this.drinkRepository = drinkRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> getStockByBranch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }

        return stockRepository.findByBranch_Id(branchId);
    }

    @Override
    public Stock restockBranch(UUID branchId, Integer quantity, UUID drinkId) {
        if(branchId == null || quantity == null || drinkId == null) {
            throw new RuntimeException("All fields are required");
        }

        if(quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Drink drink = drinkRepository.findById(drinkId).orElseThrow(() -> new RuntimeException("Drink not found"));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch not found"));

        Optional<Stock> existingStock = stockRepository.findByDrink_IdAndBranch_Id(drinkId, branchId);
        if(existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            return stockRepository.save(stock);
        } else{
            Stock newStock = new Stock();
            newStock.setDrink(drink);
            newStock.setBranch(branch);
            newStock.setQuantity(quantity);
            newStock.setMinThreshold(10);
            return stockRepository.save(newStock);
        }
    }

    @Override
    public Stock getStockByDrinkAndBranch(UUID drinkId, UUID branchId) {
        drinkRepository.findById(drinkId).orElseThrow(() -> new RuntimeException("Drink not found"));
        return stockRepository.findByDrink_IdAndBranch_Id(drinkId, branchId).orElseThrow(() -> new RuntimeException("Stock not found"));
    }
}
