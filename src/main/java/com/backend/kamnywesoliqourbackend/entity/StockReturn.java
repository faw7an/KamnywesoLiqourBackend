package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.ReturnStatus;
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
@Table(name = "stock_returns")
public class StockReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private Integer quantity;
    private String reason;
    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
    private String hqResponse;
    @ManyToOne
    @JoinColumn(name = "raised_by")
    private User raisedBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
