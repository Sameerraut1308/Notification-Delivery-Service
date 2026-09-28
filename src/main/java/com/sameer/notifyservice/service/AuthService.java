package com.sameer.notifyservice.service;

import com.sameer.notifyservice.config.JwtUtil;
import com.sameer.notifyservice.dto.AuthResponse;
import com.sameer.notifyservice.dto.LoginRequest;
import com.sameer.notifyservice.dto.RegisterRequest;
import com.sameer.notifyservice.exception.BadRequestException;
import com.sameer.notifyservice.exception.ResourceNotFoundException;
import com.sameer.notifyservice.model.User;
import com.sameer.notifyservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered: " + request.getEmail());
        }

        User.Role assignedRole = (request.getRole() != null) ? request.getRole() : User.Role.OPERATOR;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(assignedRole)
                .build();

        User savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser, "User registered successfully");
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Authenticate credentials (throws BadCredentialsException if invalid)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()));

        // 2. Fetch user to retrieve role and id
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // 3. Generate token and return response
        return buildAuthResponse(user, "Login successful");
    }

    private AuthResponse buildAuthResponse(User user, String message) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        String token = jwtUtil.generateToken(claims, user.getEmail());

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .message(message)
                .build();
    }
}