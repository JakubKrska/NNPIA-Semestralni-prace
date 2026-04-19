package com.example.petcare.dto;

import lombok.Data;

@Data
public class AppUserRequestDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}