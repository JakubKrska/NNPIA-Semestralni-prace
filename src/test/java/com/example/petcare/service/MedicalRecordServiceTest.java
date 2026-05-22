package com.example.petcare.service;

import com.example.petcare.entity.AppUser;
import com.example.petcare.entity.Pet;
import com.example.petcare.repository.MedicalRecordRepository;
import com.example.petcare.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private MedicalRecordRepository recordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Test
    public void getRecordsByPetId_ShouldReturnRecords_WhenUserIsOwner() {
        // Arrange
        String ownerEmail = "majitel@seznam.cz";
        AppUser owner = new AppUser();
        owner.setEmail(ownerEmail);

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setOwner(owner);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        // Act & Assert (Ověříme, že pro majitele metoda projde bez výjimky)
        assertDoesNotThrow(() -> medicalRecordService.getRecordsByPetId(1L, ownerEmail));
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    public void getRecordsByPetId_ShouldThrowException_WhenUserIsNotOwner() {
        // Arrange
        AppUser realOwner = new AppUser();
        realOwner.setEmail("skutecny-majitel@seznam.cz");

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setOwner(realOwner);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        // Act & Assert (Ověříme, že útočník/cizí uživatel dostane RuntimeException)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicalRecordService.getRecordsByPetId(1L, "utocnik@seznam.cz");
        });

        assertEquals("Nemáte oprávnění k datům tohoto mazlíčka", exception.getMessage());
    }
}