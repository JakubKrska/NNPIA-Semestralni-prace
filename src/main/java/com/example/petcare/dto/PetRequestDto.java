package com.example.petcare.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PetRequestDto {
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private Double weight;
}