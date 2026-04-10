package com.backend.kamnywesoliqourbackend.controller;


import com.backend.kamnywesoliqourbackend.dto.res.LoyaltyCustomerRes;
import com.backend.kamnywesoliqourbackend.dto.res.LoyaltyTransactionRes;
import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import com.backend.kamnywesoliqourbackend.entity.LoyaltyTransaction;
import com.backend.kamnywesoliqourbackend.service.interfaces.LoyaltyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<LoyaltyCustomerRes>> searchCustomer(@RequestParam String q) {
        List<LoyaltyCustomer> customers = loyaltyService.getLoyaltyCustomer(q);
        return ResponseEntity.ok(
                customers.stream()
                        .map(this::mapToLoyaltyRes)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<LoyaltyCustomerRes> register(@RequestBody LoyaltyCustomer customer) {
        LoyaltyCustomer createdCustomer = loyaltyService.registerCustomer(customer);
        return ResponseEntity.ok(mapToLoyaltyRes(createdCustomer));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<LoyaltyTransactionRes>> getHistory(@PathVariable UUID id) {
        List<LoyaltyTransaction> transactions = loyaltyService.getLoyaltyTransactions(id);
        return ResponseEntity.ok(
                transactions.stream()
                        .map(this::mapToTransactionRes)
                        .toList()
        );
    }

    private LoyaltyTransactionRes mapToTransactionRes(LoyaltyTransaction t) {
        return new LoyaltyTransactionRes(
                t.getId(),
                t.getPointsEarned(),
                t.getPointsRedeemed(),
                t.getTransactionType(),
                t.getOrder() != null ? t.getOrder().getId() : null,
                t.getCreatedAt()
        );
    }
    private LoyaltyCustomerRes mapToLoyaltyRes(LoyaltyCustomer customer){
        return new LoyaltyCustomerRes(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getCardId(),
                customer.getTier(),
                customer.getPointsBalance()
        );
    }
}
