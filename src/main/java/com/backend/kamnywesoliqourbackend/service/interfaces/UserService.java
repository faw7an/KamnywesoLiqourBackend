package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Session;
import com.backend.kamnywesoliqourbackend.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID id);
    List<User> getPendingUsers();
    void approveUser(UUID id);
//    void rejectUser(UUID id);
    void suspendUser(UUID id);
    void deleteUser(UUID id);
    List<Session> getAllSessions();
    void deleteSessionById(UUID id);
    void deleteAllSessions();
}
