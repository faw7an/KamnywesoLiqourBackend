package com.backend.kamnywesoliqourbackend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dispatch_items")
public class DispatchItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dispatch_order_id")
    private DispatchOrder dispatchOrder;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
    private Integer quantity;
}
