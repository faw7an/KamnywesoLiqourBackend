package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.req.DispatchReq;
import com.backend.kamnywesoliqourbackend.dto.res.DispatchItemRes;
import com.backend.kamnywesoliqourbackend.dto.res.DispatchOrderRes;
import com.backend.kamnywesoliqourbackend.entity.DispatchItem;
import com.backend.kamnywesoliqourbackend.entity.DispatchOrder;
import com.backend.kamnywesoliqourbackend.service.interfaces.DispatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {
    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<DispatchOrderRes>> fetchBranchDispatches(@PathVariable UUID branchId){
        if (branchId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<DispatchOrder> dispatchOrders = dispatchService.getBranchDispatch(branchId);
        return ResponseEntity.ok(
                dispatchOrders.stream()
                        .map(this::mapToDispatchOrderRes)
                        .toList()
        );
    }
    private DispatchOrderRes mapToDispatchOrderRes(DispatchOrder dispatchOrder){
        // Map the line items first
        List<DispatchItemRes> dispatchItemDto = dispatchOrder.getDispatchItems().stream().map(
                item -> new DispatchItemRes(
                        item.getId(),
                        item.getDrink().getId(),
                        item.getDrink().getName(),
                        item.getDrink().getBrand(),
                        item.getQuantity()
                )
        ).toList();

        // Map the main order (Must match the 7 fields in your record)
        return new DispatchOrderRes(
                dispatchOrder.getId(),                // 1. id
                dispatchOrder.getBranch().getId(),     // 2. branchId
                dispatchOrder.getBranch().getName(),   // 3. branchName
                dispatchOrder.getStatus(),             // 4. status
                dispatchOrder.getDriverName(),         // 5. driverName
                dispatchOrder.getVehiclePlate(),       // 6. vehiclePlate
                dispatchItemDto                        // 7. items
        );
    }

    @PostMapping
    public ResponseEntity<DispatchOrderRes> createDispatch(@RequestBody DispatchReq req) {
        DispatchOrder created = dispatchService.createDispatch(req);
        return ResponseEntity.ok(mapToDispatchOrderRes(created));
    }

    @PatchMapping("/{id}/confirm/{branchId}")
    public ResponseEntity<DispatchOrderRes> confirmDispatch(
            @PathVariable UUID id,
            @PathVariable UUID branchId) {

        DispatchOrder confirmed = dispatchService.confirmBranchDispatch(id, branchId);
        return ResponseEntity.ok(mapToDispatchOrderRes(confirmed));
    }
}
