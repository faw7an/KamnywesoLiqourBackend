package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.BranchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public Branch getBranchById(UUID id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    @Override
    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranch(UUID id, Branch branchUpdates) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        existingBranch.setName(branchUpdates.getName());
        existingBranch.setLocation(branchUpdates.getLocation());
        existingBranch.setHq(branchUpdates.isHq());

        if (branchUpdates.getManager() != null) {
            existingBranch.setManager(branchUpdates.getManager());
        }

        return branchRepository.save(existingBranch);
    }

    @Override
    public void deleteBranch(UUID id) {
        branchRepository.deleteById(id);
    }

    @Override
    public Branch getHqBranch() {
        return branchRepository.getBranchesByIsHq(true);
    }
}
