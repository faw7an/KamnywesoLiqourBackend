package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Branch;

import java.util.List;
import java.util.UUID;

public interface BranchService {
    List<Branch> getAllBranches();
    Branch getBranchById(UUID id);
    Branch createBranch(Branch branch);
    Branch updateBranch(UUID id , Branch branch);
    void deleteBranch(UUID id);
    Branch getHqBranch();
}
