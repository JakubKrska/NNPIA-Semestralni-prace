package com.example.petcare.controller;

import com.example.petcare.entity.Pet;
import com.example.petcare.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetRepository petRepository;

    @GetMapping
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        return petRepository.save(pet);
    }

    @GetMapping("/{id}")
    public Pet getPet(@PathVariable Long id) {
        return petRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet != null) {
            pet.setName(petDetails.getName());
            pet.setSpecies(petDetails.getSpecies());
            pet.setWeight(petDetails.getWeight());
            return petRepository.save(pet);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petRepository.deleteById(id);
    }
}
