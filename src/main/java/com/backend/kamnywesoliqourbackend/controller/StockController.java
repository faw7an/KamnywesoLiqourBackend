package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.req.RestockReq;
import com.backend.kamnywesoliqourbackend.dto.res.StockRes;
import com.backend.kamnywesoliqourbackend.entity.Stock;
import com.backend.kamnywesoliqourbackend.service.interfaces.BranchService;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;
    private final BranchService branchService;

    StockController(StockService stockService, BranchService branchService) {
        this.stockService = stockService;
        this.branchService = branchService;
    }

    @GetMapping
    public ResponseEntity<List<StockRes>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(
                stocks.stream().map(this::mapToStockRes).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<StockRes>> getStockByBranch(@PathVariable UUID id) {
        List<Stock> stock = stockService.getStockByBranch(id);
        return ResponseEntity.ok(stock.stream().map(this::mapToStockRes).toList());
    }

    @PostMapping("/restock/")
    public ResponseEntity<StockRes> restockStock(@RequestBody RestockReq req) {
        Stock updatedStock = stockService.restockBranch(req.branchId(), req.quantity(), req.drinkId());
        return ResponseEntity.ok(mapToStockRes(updatedStock));
    }


    private StockRes mapToStockRes(Stock stock) {
        return new StockRes(
                stock.getId(),
                stock.getDrink().getId(),
                stock.getDrink().getName(),
                stock.getQuantity(),
                stock.getMinThreshold(),
                stock.getLastRestockedAt()
        );
    }

}
