package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import com.backend.kamnywesoliqourbackend.entity.LoyaltyTransaction;
import com.backend.kamnywesoliqourbackend.enums.TransactionType;

import java.util.List;
import java.util.UUID;

public interface LoyaltyService {
    List<LoyaltyCustomer> getAllLoyaltyCustomers();
    List<LoyaltyCustomer> getLoyaltyCustomer(String query);
    LoyaltyCustomer getLoyaltyCustomerById(UUID customerId);
    LoyaltyCustomer registerCustomer(LoyaltyCustomer customer);
    List<LoyaltyTransaction> getLoyaltyTransactions(UUID customerId);
    LoyaltyTransaction createLoyaltyTransaction(UUID customerId, UUID orderId, TransactionType type , Integer points);

}
