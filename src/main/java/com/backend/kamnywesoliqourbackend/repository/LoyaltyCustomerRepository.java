package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.LoyaltyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoyaltyCustomerRepository extends JpaRepository<LoyaltyCustomer, UUID> {
    List<LoyaltyCustomer> findByNameContainingOrPhoneContainingOrCardIdContaining(String query, String query1, String query2);
}
