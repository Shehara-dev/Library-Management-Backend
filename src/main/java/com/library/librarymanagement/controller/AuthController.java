package com.library.librarymanagement.controller;

import com.library.librarymanagement.config.JwtUtil;
import com.library.librarymanagement.dto.AuthResponse;
import com.library.librarymanagement.dto.LoginRequest;
import com.library.librarymanagement.dto.SignupRequest;
import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.service.EmailService;
import com.library.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;
    
   @PostMapping("/signup")
public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    try {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setRole(signupRequest.getRole());
        
        User savedUser = userService.registerUser(user);
        
        // Send welcome email
        emailService.sendWelcomeEmail(
            savedUser.getEmail(), 
            savedUser.getEmail(), 
            savedUser.getRole().name()
        );
        
        return ResponseEntity.ok(new AuthResponse(
            null,
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            "User registered successfully"
        ));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}  
    
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        User user = userService.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getIsBlacklisted()) {
            return ResponseEntity.badRequest().body("User is blacklisted");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        
        return ResponseEntity.ok(new AuthResponse(
            token,
            user.getId(),  // Add this line
            user.getEmail(),
            user.getRole().name(),
            "Login successful"
        ));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Invalid credentials");
    }
}
}