package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.req.StockReturnReq;
import com.backend.kamnywesoliqourbackend.dto.res.StockRes;
import com.backend.kamnywesoliqourbackend.dto.res.StockReturnRes;
import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockReturnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/returns")
public class StockReturnController {
    private final StockReturnService stockReturnService;

    public StockReturnController(StockReturnService stockReturnService) {
        this.stockReturnService = stockReturnService;
    }

    @GetMapping
    public ResponseEntity<List<StockReturnRes>> getAllReturns() {
        return ResponseEntity.ok(stockReturnService.getAllStockReturns().stream().map(this::mapToRes).toList());
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<StockReturnRes>> getBranchReturns(@PathVariable UUID branchId) {
        return ResponseEntity.ok(stockReturnService.getStockReturnsByBranch(branchId).stream().map(this::mapToRes).toList());
    }

    @PostMapping
    public ResponseEntity<StockReturnRes> createReturn(@RequestBody StockReturnReq req) {
        // This now matches the DTO you are actually returning
        StockReturn created = stockReturnService.processStockReturn(req);
        return ResponseEntity.ok(mapToRes(created));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StockReturnRes> updateStatus(
            @PathVariable UUID id,
            @RequestParam ReturnStatus status,
            @RequestParam String response) {
        StockReturn updated = stockReturnService.updateStockReturnStatus(id, status, response);
        return ResponseEntity.ok(mapToRes(updated));
    }

    private StockReturnRes mapToRes(StockReturn sr) {
        return new StockReturnRes(
                sr.getId(),
                sr.getBranch().getName(),
                sr.getDrink().getName(),
                sr.getQuantity(),
                sr.getReason(),
                sr.getStatus(),
                sr.getHqResponse(),
                sr.getCreatedAt()
        );
    }
}