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
        String status = "OK";
        if (stock.getQuantity() == 0) status = "Out of Stock";
        else if (stock.getQuantity() < stock.getMinThreshold()) status = "Critical";
        else if (stock.getQuantity() == stock.getMinThreshold()) status = "Low";

        String restockedDate = stock.getLastRestockedAt() != null 
            ? java.time.format.DateTimeFormatter.ofPattern("dd MMM").format(stock.getLastRestockedAt())
            : "N/A";

        return new StockRes(
                stock.getId(),
                stock.getDrink().getId(),
                stock.getDrink().getName(),
                stock.getDrink().getBrand(),
                stock.getBranch() != null ? stock.getBranch().getName() : "HQ",
                stock.getQuantity(),
                stock.getMinThreshold(),
                status,
                restockedDate,
                stock.getDrink().getPrice(),
                stock.getDrink().getImage()
        );
    }

}
