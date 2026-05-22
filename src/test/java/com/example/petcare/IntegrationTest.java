package com.example.petcare;

import com.example.petcare.dto.AppUserRequestDto;
import com.example.petcare.dto.AppUserResponseDto;
import com.example.petcare.dto.PetRequestDto;
import com.example.petcare.entity.Pet;
import com.example.petcare.service.AppUserService;
import com.example.petcare.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Po každém testu se databáze vrátí do původního stavu
public class IntegrationTest {

    @Autowired
    private AppUserService userService;

    @Autowired
    private PetService petService;

    @Test
    public void userRegistrationAndPetCreationIntegrationFlow() {
        // 1. Krok: Registrace uživatele přes AppUserService
        AppUserRequestDto userDto = new AppUserRequestDto();
        userDto.setEmail("integrace@test.cz");
        userDto.setPassword("Heslo123");
        userDto.setFirstName("Jan");
        userDto.setLastName("Novák");

        AppUserResponseDto savedUser = userService.registerUser(userDto);
        assertNotNull(savedUser.getId());

        // 2. Krok: Vytvoření mazlíčka pro tohoto uživatele přes PetService
        PetRequestDto petDto = new PetRequestDto();
        petDto.setName("Blesk");
        petDto.setSpecies("Kočka");
        petDto.setBreed("Perská");
        petDto.setBirthDate(LocalDate.now().minusYears(2));
        petDto.setWeight(4.5);

        Pet savedPet = petService.createPet(petDto, "integrace@test.cz");
        assertNotNull(savedPet.getId());
        assertEquals("integrace@test.cz", savedPet.getOwner().getEmail());

        // 3. Krok: Ověření, že služba správně provázala data v DB a vrátí mazlíčka v seznamu
        List<Pet> userPets = petService.getPetsForUser("integrace@test.cz");
        assertEquals(1, userPets.size());
        assertEquals("Blesk", userPets.get(0).getName());
    }
}