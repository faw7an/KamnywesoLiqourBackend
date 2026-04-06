package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import com.backend.kamnywesoliqourbackend.repository.UserRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User register(String name, String email, String password, String phone) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("User with email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setPhone(phone);
        user.setStatus(UserStatus.PENDING);
        userRepository.save(user);
        return user;
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with email does not exist"));
        if(!user.getPasswordHash().equals(password)){
            throw new RuntimeException("Invalid Credentials");
        }
        return "token";
    }

    @Override
    public void logout(String token) {
//        logs out the user
    }

    @Override
    public Boolean verifyOtp(String email, String otp) {
        return null;
    }

    @Override
    public String refreshToken(String token) {
        return "";
    }
}
