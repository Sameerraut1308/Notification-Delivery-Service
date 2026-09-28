package com.sameer.notifyservice.service;

import com.sameer.notifyservice.config.JwtUtil;
import com.sameer.notifyservice.dto.AuthResponse;
import com.sameer.notifyservice.dto.RegisterRequest;
import com.sameer.notifyservice.exception.BadRequestException;
import com.sameer.notifyservice.model.User;
import com.sameer.notifyservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // 1. Check if email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered: " + request.getEmail());
        }

        // 2. Default to OPERATOR role if not specified
        User.Role assignedRole = (request.getRole() != null) ? request.getRole() : User.Role.OPERATOR;

        // 3. Hash password using BCrypt and save User
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(assignedRole)
                .build();

        User savedUser = userRepository.save(user);

        // 4. Generate JWT with the user's role embedded in claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", savedUser.getRole().name());
        String token = jwtUtil.generateToken(claims, savedUser.getEmail());

        return AuthResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .token(token)
                .message("User registered successfully")
                .build();
    }
}