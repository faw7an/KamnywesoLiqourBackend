package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.DispatchStatus;
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
@Table(name = "dispatch_orders")
public class DispatchOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    private String driverName;
    private String vehiclePlate;
    private String notes;
    @Enumerated(EnumType.STRING)
    private DispatchStatus status;
    private LocalDateTime dispatchedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime receivedAt;
}
