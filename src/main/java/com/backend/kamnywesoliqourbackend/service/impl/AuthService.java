package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.dto.req.auth.LoginReq;
import com.backend.kamnywesoliqourbackend.dto.req.auth.RegisterReq;
import com.backend.kamnywesoliqourbackend.dto.req.auth.VerifyOtpReq;
import com.backend.kamnywesoliqourbackend.dto.res.auth.AuthRes;
import com.backend.kamnywesoliqourbackend.dto.res.auth.MessageRes;
import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.enums.Role;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import com.backend.kamnywesoliqourbackend.repository.UserRepository;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.security.JwtService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {
    private final UserRepository repository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    public AuthService(UserRepository repository, BranchRepository branchRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, JavaMailSender mailSender) {
        this.repository = repository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
    }

    public MessageRes register(RegisterReq request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.PENDING);

        // Convert string role to enum, default to CASHIER if invalid
        try {
            String roleStr = request.role().toUpperCase().replace(" ", "_");
            if (roleStr.equals("BRANCH_MANAGER")) roleStr = "MANAGER";
            if (roleStr.equals("ADMINISTRATOR")) roleStr = "ADMIN";
            user.setRole(Role.valueOf(roleStr));
        } catch (Exception e) {
            user.setRole(Role.CASHIER);
        }

        // Set branch if provided
        if (request.branchName() != null && !request.branchName().isEmpty()) {
            Branch branch = branchRepository.findByName(request.branchName())
                    .orElse(branchRepository.getBranchesByIsHq(true)); // Fallback to HQ
            user.setBranch(branch);
        }

        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        
        repository.save(user);
        sendOtpEmail(user.getEmail(), otp);
        
        return new MessageRes("Registration successful. OTP sent to your email.");
    }

    public MessageRes login(LoginReq request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = repository.findByEmail(request.email())
                .orElseThrow();
        
        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        repository.save(user);
        
        sendOtpEmail(user.getEmail(), otp);
        
        return new MessageRes("OTP sent to your email. Please verify.");
    }

    public AuthRes verifyOtp(VerifyOtpReq request) {
        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        if (user.getOtpCode() == null || !user.getOtpCode().equals(request.otpCode())) {
            throw new RuntimeException("Invalid OTP");
        }
        
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }
        
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        if (user.getStatus() == UserStatus.PENDING) {
            user.setStatus(UserStatus.ACTIVE);
        }
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return new AuthRes(jwtToken, "Login successful");
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        // Send ALL emails to the verified Resend address to avoid free-tier errors
        message.setTo("fauzdasoodais@gmail.com");
        message.setFrom("onboarding@resend.dev");
        message.setSubject("Your Login OTP");
        message.setText("Your OTP is: " + otp + ". It is valid for 5 minutes.");
        try {
            mailSender.send(message);
            System.out.println("OTP sent to fauzdasoodais@gmail.com (Original target: " + to + "): " + otp);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            System.err.println("Generated OTP was: " + otp);
        }
    }
}
