package com.example.petcare.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class AppUserResponseDto {
    private Long id;
    private String email;
    private boolean active;
    private String firstName;
    private String lastName;
}