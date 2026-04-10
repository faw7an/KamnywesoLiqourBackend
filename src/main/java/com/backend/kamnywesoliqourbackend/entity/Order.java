package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private String customerName;
    private String customerPhone;
    @ManyToOne
    @JoinColumn(name = "loyalty_card_id")
    private LoyaltyCustomer loyaltyCustomer;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
