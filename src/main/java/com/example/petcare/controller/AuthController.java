package com.example.petcare.controller;

import com.example.petcare.config.JwtUtils;
import com.example.petcare.dto.AppUserRequestDto;
import com.example.petcare.dto.AppUserResponseDto;
import com.example.petcare.entity.AppUser;
import com.example.petcare.repository.AppUserRepository;
import com.example.petcare.service.AppUserService;
import com.example.petcare.service.CustomUserDetailsService; // Přidán import
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails; // Přidán import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AppUserService appUserService;
    private final CustomUserDetailsService userDetailsService; // Přidána závislost na service pro UserDetails

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUserRequestDto dto) {
        try {
            AppUserResponseDto response = appUserService.registerUser(dto);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginData) {
        AppUser user = userRepository.findByEmail(loginData.get("email"))
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen"));

        if (passwordEncoder.matches(loginData.get("password"), user.getPassword())) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

            String token = jwtUtils.generateToken(userDetails);
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Špatné heslo");
        }
    }
}