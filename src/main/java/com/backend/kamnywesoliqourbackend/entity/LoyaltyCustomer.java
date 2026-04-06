package com.backend.kamnywesoliqourbackend.entity;


import com.backend.kamnywesoliqourbackend.enums.LoyaltyTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loyalty_customers")
public class LoyaltyCustomer {
//    loyalty_customers (id, name, phone, card_id, tier, points_balance,
//                       branch_id, registered_at)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String phone;
    private String cardId;
    @Enumerated(EnumType.STRING)
    private LoyaltyTier tier;
    private Integer pointsBalance;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @CreationTimestamp
    private LocalDateTime registeredAt;
}
