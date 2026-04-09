package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.req.CreateBranchReq;
import com.backend.kamnywesoliqourbackend.dto.res.BranchRes;
import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.service.interfaces.BranchService;
import com.backend.kamnywesoliqourbackend.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    private final BranchService branchService;
    private final UserService userService;

    public BranchController(BranchService branchService , UserService userService) {
        this.branchService = branchService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<BranchRes>> getAllBranches(){
        List <Branch> branches = branchService.getAllBranches();

        List <BranchRes> response = branches.stream()
                .map(branch -> new BranchRes(
                        branch.getId(),
                        branch.getName(),
                        branch.getLocation(),
                        branch.getManager() == null ? "No manager found" : branch.getManager().getName() ,
                        branch.isHq()
                ))
                .toList();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchRes> getBranchById(@PathVariable UUID id){
        Branch branch = branchService.getBranchById(id);
        return ResponseEntity.ok(new BranchRes(
                branch.getId(),
                branch.getName(),
                branch.getLocation(),
                branch.getManager() == null ? "No manager found": branch.getManager().getName(),
                branch.isHq()
        ));
    }

    @PostMapping
    public ResponseEntity<BranchRes> createBranch (@RequestBody CreateBranchReq req){
        Branch branch = new Branch();
        branch.setName(req.name());
        branch.setLocation(req.location());
        branch.setHq(req.isHq());

        if(req.managerId() != null){
            branch.setManager(userService.getUserById(req.managerId()));
        }
        Branch createdBranch = branchService.createBranch(branch);

        return ResponseEntity.ok(new BranchRes(
                createdBranch.getId(),
                createdBranch.getName(),
                createdBranch.getLocation(),
                createdBranch.getManager() == null ? "No manager found" : createdBranch.getManager().getName(),
                createdBranch.isHq()
        ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id){
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchRes> updateBranch(@PathVariable UUID id , @RequestBody CreateBranchReq req){
        Branch branch = branchService.getBranchById(id);
        branch.setName(req.name());
        branch.setLocation(req.location());
        branch.setHq(req.isHq());

        if(req.managerId() != null){
            branch.setManager(userService.getUserById(req.managerId()));
        }

        Branch updatedBranch = branchService.updateBranch(id,branch);
        return ResponseEntity.ok(new BranchRes(
                updatedBranch.getId(),
                updatedBranch.getName(),
                updatedBranch.getLocation(),
                updatedBranch.getManager() == null ? "No manager found" : updatedBranch.getManager().getName(),
                updatedBranch.isHq()
        ));
    }
}
