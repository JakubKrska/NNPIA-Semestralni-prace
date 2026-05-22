package com.example.petcare.controller;

import com.example.petcare.dto.PetRequestDto;
import com.example.petcare.entity.Pet;
import com.example.petcare.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllUserPets(Authentication auth) {

        return ResponseEntity.ok(petService.getPetsForUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody PetRequestDto dto, Authentication auth) {
        return ResponseEntity.status(201).body(petService.createPet(dto, auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(petService.getPetByIdAndUser(id, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody PetRequestDto dto, Authentication auth) {
        return ResponseEntity.ok(petService.updatePet(id, dto, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id, Authentication auth) {
        petService.deletePet(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}