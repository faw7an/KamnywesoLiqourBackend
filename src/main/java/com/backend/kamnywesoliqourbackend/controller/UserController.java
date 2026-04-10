package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.res.UserRes;
import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserRes>> getAllUsers(){
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(
                users.stream().map(this::mapToUserRes)
                .toList()
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserRes>> getPendingUsers(){
        List<User> users = userService.getPendingUsers();

        return ResponseEntity.ok(
                users.stream().map(this::mapToUserRes)
                .toList()
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<String> approveUser(@PathVariable UUID id){
        userService.approveUser(id);
        return ResponseEntity.ok("User approved successfully");
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<String> suspendUser(@PathVariable UUID id){
        userService.suspendUser(id);
        return ResponseEntity.ok("User suspended successfully");
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    private UserRes mapToUserRes(User user) {
        return new UserRes(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                user.getBranch() != null ? user.getBranch().getName() : null,
                user.getBranch() != null ? user.getBranch().getId() : null
        );
    }
}
