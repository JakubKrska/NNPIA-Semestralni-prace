package com.example.petcare.controller;

import com.example.petcare.config.JwtUtils;
import com.example.petcare.dto.AppUserRequestDto;
import com.example.petcare.dto.AppUserResponseDto;
import com.example.petcare.entity.AppUser;
import com.example.petcare.repository.AppUserRepository;
import com.example.petcare.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<AppUserResponseDto> register(@RequestBody AppUserRequestDto dto) {
        return ResponseEntity.status(201).body(appUserService.registerUser(dto));
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginData) {
        AppUser user = userRepository.findByEmail(loginData.get("email"))
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen"));

        if (passwordEncoder.matches(loginData.get("password"), user.getPassword())) {
            String token = jwtUtils.generateToken(user.getEmail());
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Špatné heslo");
        }
    }
}