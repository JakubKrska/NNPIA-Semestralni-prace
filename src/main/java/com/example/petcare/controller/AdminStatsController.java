package com.example.petcare.controller;

import com.example.petcare.repository.PetRepository;
import com.example.petcare.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        // Vytáhneme globální počty z databáze
        long totalPets = petRepository.count();
        long totalUsers = userRepository.count();

        stats.put("totalPets", totalPets);
        stats.put("totalUsers", totalUsers);
        stats.put("systemStatus", "OPERATIONAL");
        stats.put("apiVersion", "v4.0 (Tailwind-ready)");

        return ResponseEntity.ok(stats);
    }
}