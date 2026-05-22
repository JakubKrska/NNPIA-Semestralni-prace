package com.example.petcare.controller;

import com.example.petcare.entity.Pet;
import com.example.petcare.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @Test
    @WithMockUser(username = "test@user.com")
    public void getAllUserPets_ShouldReturnListAnd200Ok() throws Exception {
        // Arrange
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Alík");

        when(petService.getPetsForUser("test@user.com")).thenReturn(List.of(pet));

        // Act & Assert
        mockMvc.perform(get("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alík"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void getAllUserPets_WithoutToken_ShouldReturn403Forbidden() throws Exception {
        // Act & Assert (Ověření, že bez přihlášení Spring Security požadavek nepustí)
        mockMvc.perform(get("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}