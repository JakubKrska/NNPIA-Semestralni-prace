package com.example.petcare.service;

import com.example.petcare.dto.PetRequestDto;
import com.example.petcare.entity.AppUser;
import com.example.petcare.entity.Pet;
import com.example.petcare.repository.AppUserRepository;
import com.example.petcare.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final AppUserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Pet> getPetsForUser(String email) {
        return petRepository.findByOwnerEmail(email);
    }

    @Transactional
    public Pet createPet(PetRequestDto dto, String email) {
        AppUser owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen"));

        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setBirthDate(dto.getBirthDate());
        pet.setWeight(dto.getWeight());
        pet.setOwner(owner);

        return petRepository.save(pet);
    }

    @Transactional(readOnly = true)
    public Pet getPetByIdAndUser(Long id, String email) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mazlíček nenalezen"));

        validateOwnership(pet, email);
        return pet;
    }

    @Transactional
    public Pet updatePet(Long id, PetRequestDto dto, String email) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mazlíček nenalezen"));

        validateOwnership(pet, email);

        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setBirthDate(dto.getBirthDate());
        pet.setWeight(dto.getWeight());

        return petRepository.save(pet);
    }

    @Transactional
    public void deletePet(Long id, String email) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mazlíček nenalezen"));

        validateOwnership(pet, email);
        petRepository.delete(pet);
    }

    private void validateOwnership(Pet pet, String email) {
        if (!pet.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("Nemáte oprávnění k tomuto mazlíčkovi.");
        }
    }
}