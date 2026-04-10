package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.dto.req.DispatchItemReq;
import com.backend.kamnywesoliqourbackend.dto.req.DispatchReq;
import com.backend.kamnywesoliqourbackend.entity.*;
import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;
import com.backend.kamnywesoliqourbackend.repository.*;
import com.backend.kamnywesoliqourbackend.service.interfaces.DispatchService;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DispatchServiceImpl implements DispatchService {
    private final DispatchOrderRepository dispatchOrderRepository;
    private final DispatchItemRepository dispatchItemRepository;
    private final StockService stockService;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;

    public DispatchServiceImpl(DispatchOrderRepository dispatchOrderRepository, DispatchItemRepository dispatchItemRepository, StockService stockService, BranchRepository branchRepository, UserRepository userRepository, DrinkRepository drinkRepository){
        this.dispatchOrderRepository = dispatchOrderRepository;
        this.dispatchItemRepository = dispatchItemRepository;
        this.stockService = stockService;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
    }

    @Override
    public List<DispatchOrder> getAllDispatches() {
        return dispatchOrderRepository.findAll();
    }

//    @Override
//    public DispatchOrder createDispatch(DispatchOrder dispatchOrder) {
//        dispatchOrderRepository.save(dispatchOrder);
//        return dispatchOrder;
//    }

    @Override
    public DispatchOrder approveDispatch(UUID id) {
        DispatchOrder dispatchOrder = dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(DispatchStatus.APPROVED);
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public DispatchOrder getDispatch(UUID id) {
        if(id == null) {
            throw new RuntimeException("Dispatch id is required");
        }
        return dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
    }
    @Override
    @Transactional
    public DispatchOrder createDispatch(DispatchReq req) {
        // 1. Lookup Branch and User
        Branch branch = branchRepository.findById(req.branchId()).orElseThrow(()-> new RuntimeException("Branch not found"));
        User user = userRepository.findById(req.createdBy()).orElseThrow(()-> new RuntimeException("User not found"));

        // 2. Create DispatchOrder entity
        DispatchOrder dispatch = new DispatchOrder();
        dispatch.setBranch(branch);
        dispatch.setCreatedBy(user);
        dispatch.setStatus(DispatchStatus.PENDING);
        dispatch.setDriverName(req.driverName());
        dispatch.setVehiclePlate(req.vehiclePlate());
        dispatch.setDispatchedAt(LocalDateTime.now());
        dispatch.setNotes(req.notes());

        List<DispatchItem> items = new ArrayList<>();
        for (DispatchItemReq itemReq : req.items()) {
            Drink drink = drinkRepository.findById(itemReq.drinkId())
                    .orElseThrow(() -> new RuntimeException("Drink not found"));

            DispatchItem dispatchItem = new DispatchItem();
            dispatchItem.setDispatchOrder(dispatch); // Set the parent link
            dispatchItem.setDrink(drink);
            dispatchItem.setQuantity(itemReq.quantity());

            items.add(dispatchItem);
        }

        dispatch.setDispatchItems(items);

        // 4. Save (Cascade will save the items too)
        return dispatchOrderRepository.save(dispatch);
    }

    @Override
    public DispatchOrder updateDispatchStatus(UUID id, DispatchStatus status) {
        if(id == null || status == null) {
            throw new RuntimeException("Dispatch id and status are required");
        }
        DispatchOrder dispatchOrder = dispatchOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Dispatch not found"));
        dispatchOrder.setStatus(status);
        dispatchOrderRepository.save(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    public List<DispatchOrder> getBranchDispatch(UUID branchId) {
        if(branchId == null) {
            throw new RuntimeException("Branch id is required");
        }
        return dispatchOrderRepository.findByBranch_Id(branchId);
    }

    @Override
    @Transactional
    public DispatchOrder confirmBranchDispatch(UUID id, UUID branchId) {
        if(id == null || branchId == null) {
            throw new RuntimeException("Dispatch id and branch id are required");
        }
        DispatchOrder dispatch = dispatchOrderRepository.findByIdAndBranch_Id(id, branchId).orElseThrow(()-> new RuntimeException("Dispatch not found"));

        if (dispatch.getStatus() != DispatchStatus.DISPATCHED) {
            throw new RuntimeException("Cannot confirm a dispatch that hasn't been sent yet!");
        }
        if(dispatch.getStatus() == DispatchStatus.RECEIVED) {
            throw new RuntimeException("Dispatch already confirmed");
        }

        dispatch.setStatus(DispatchStatus.RECEIVED);
        dispatch.setReceivedAt(LocalDateTime.now());


        for(DispatchItem item : dispatch.getDispatchItems()) {
            stockService.restockBranch(branchId, item.getQuantity(), item.getDrink().getId());
        }

        return dispatchOrderRepository.save(dispatch);
    }
}
