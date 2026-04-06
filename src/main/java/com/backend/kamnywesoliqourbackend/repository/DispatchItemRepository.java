package com.backend.kamnywesoliqourbackend.repository;

import com.backend.kamnywesoliqourbackend.entity.DispatchItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DispatchItemRepository extends JpaRepository<DispatchItem, UUID> {
}
