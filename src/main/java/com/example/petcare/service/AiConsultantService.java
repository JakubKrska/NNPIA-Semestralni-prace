package com.example.petcare.service;

import com.example.petcare.entity.Pet;
import com.example.petcare.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiConsultantService {

    private final PetRepository petRepository;

    public String generateConsultationPrompt(Long petId, String userQuestion) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mazlíček nenalezen"));

        // Sestavení kontextu pro AI
        return String.format(
                "Jsi veterinární asistent. Pacient: %s, Druh: %s, Váha: %.1f kg. " +
                        "Historie záznamů: %d záznamů. " +
                        "Dotaz majitele: %s. " +
                        "Na základě těchto dat poskytni doporučení.",
                pet.getName(), pet.getSpecies(), pet.getWeight(),
                pet.getMedicalRecords().size(), userQuestion
        );
    }
}