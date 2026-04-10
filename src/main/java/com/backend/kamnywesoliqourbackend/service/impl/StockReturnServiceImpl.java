package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.dto.req.StockReturnReq;
import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.entity.Drink;
import com.backend.kamnywesoliqourbackend.entity.StockReturn;
import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.repository.DrinkRepository;
import com.backend.kamnywesoliqourbackend.repository.StockReturnRepository;
import com.backend.kamnywesoliqourbackend.repository.UserRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.StockReturnService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockReturnServiceImpl implements StockReturnService {
    private final StockReturnRepository stockReturnRepository;
    private final BranchRepository branchRepository;
    private final DrinkRepository drinkRepository;
    private final UserRepository userRepository;

    public StockReturnServiceImpl(StockReturnRepository stockReturnRepository, BranchRepository branchRepository, DrinkRepository drinkRepository, UserRepository userRepository) {
        this.stockReturnRepository = stockReturnRepository;
        this.branchRepository = branchRepository;
        this.drinkRepository = drinkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<StockReturn> getAllStockReturns() {
        return stockReturnRepository.findAll();
    }
    @Override
    public StockReturn processStockReturn(StockReturnReq req) {
        Branch branch = branchRepository.findById(req.branchId()).orElseThrow(() -> new RuntimeException("Branch not found"));
        Drink drink = drinkRepository.findById(req.drinkId()).orElseThrow(() -> new RuntimeException("Drink not found"));
        User user = userRepository.findById(req.raisedBy()).orElseThrow(() -> new RuntimeException("User not found"));

        StockReturn stockReturn = new StockReturn();
        stockReturn.setBranch(branch);
        stockReturn.setDrink(drink);
        stockReturn.setRaisedBy(user);
        stockReturn.setQuantity(req.quantity());
        stockReturn.setReason(req.reason());
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
    public StockReturn updateStockReturnStatus(UUID id, ReturnStatus status, String hqResponse) {
        StockReturn sr = stockReturnRepository.findById(id).orElseThrow(() -> new RuntimeException("Return not found"));
        sr.setStatus(status);
        sr.setHqResponse(hqResponse);
        return stockReturnRepository.save(sr);
    }
}
