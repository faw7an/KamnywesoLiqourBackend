package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import com.backend.kamnywesoliqourbackend.entity.LoyaltyTransaction;
import com.backend.kamnywesoliqourbackend.enums.TransactionType;
import com.backend.kamnywesoliqourbackend.repository.LoyaltyCustomerRepository;
import com.backend.kamnywesoliqourbackend.repository.LoyaltyTransactionRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.LoyaltyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
    private final LoyaltyCustomerRepository loyaltyCustomerRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;
    private final OrderServiceImpl orderService;

    public LoyaltyServiceImpl(LoyaltyCustomerRepository loyaltyCustomerRepository , LoyaltyTransactionRepository loyaltyTransactionRepository, OrderServiceImpl orderService){
        this.loyaltyCustomerRepository = loyaltyCustomerRepository;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.orderService = orderService;
    }

    @Override
    public List<LoyaltyCustomer> getAllLoyaltyCustomers() {
        return loyaltyCustomerRepository.findAll();
    }

    @Override
    public List<LoyaltyCustomer> getLoyaltyCustomer(String query) {
        return List.of();
    }

    @Override
    public LoyaltyCustomer getLoyaltyCustomerById(UUID customerId) {
        if(customerId == null) {
            throw new RuntimeException("Customer id is required");
        }
        return loyaltyCustomerRepository.findById(customerId).orElseThrow(()-> new RuntimeException("Customer not found"));
    }

    @Override
    public LoyaltyCustomer registerCustomer(LoyaltyCustomer customer) {
        if(customer == null) {
            throw new RuntimeException("Customer is required");
        }
        return loyaltyCustomerRepository.save(customer);
    }

    @Override
    public List<LoyaltyTransaction> getLoyaltyTransactions(UUID customerId) {
        if(customerId == null) {
            throw new RuntimeException("Customer id is required");
        }
        return loyaltyTransactionRepository.findByCustomer_Id(customerId);
    }

    @Override
    public LoyaltyTransaction createLoyaltyTransaction(UUID customerId, UUID orderId, TransactionType type, Integer points) {
        if(customerId == null || orderId == null || type == null || points == null) {
            throw new RuntimeException("All fields are required");
        }

        LoyaltyCustomer customer = loyaltyCustomerRepository.findById(customerId).orElseThrow(()-> new RuntimeException("Customer not found"));

        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setCustomer(customer);
        transaction.setOrder(orderService.getOrderById(orderId));
        transaction.setTransactionType(type);

        transaction.setPointsEarned(type == TransactionType.EARNED ?  points : 0);
        transaction.setPointsEarned(type == TransactionType.EARNED ? points : 0);

        int currentPointBal = customer.getPointsBalance() == null ? 0 : customer.getPointsBalance();

        if (type == TransactionType.EARNED) {
            customer.setPointsBalance(currentPointBal + points);
        } else if (type == TransactionType.REDEEMED) {
            if (currentPointBal < points) {
                throw new RuntimeException("Insufficient points to redeem");
            }
            customer.setPointsBalance(currentPointBal - points);
        }
        loyaltyCustomerRepository.save(customer);
        loyaltyTransactionRepository.save(transaction);

        return transaction;
    }
}
