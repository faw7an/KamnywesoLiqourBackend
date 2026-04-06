package com.backend.kamnywesoliqourbackend.entity;

import com.backend.kamnywesoliqourbackend.enums.ReportTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private ReportTypes type;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    @ManyToOne
    @JoinColumn(name = "generated_by")
    private User generatedBy;
    private String filePath;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
