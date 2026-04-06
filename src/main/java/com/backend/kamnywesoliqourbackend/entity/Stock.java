package com.backend.kamnywesoliqourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private Integer quantity;
    private Integer minThreshold;
    private LocalDateTime lastRestockedAt;
}
