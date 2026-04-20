package com.example.petcare.service;

import com.example.petcare.dto.AppUserRequestDto;
import com.example.petcare.dto.AppUserResponseDto;
import com.example.petcare.entity.AppUser;
import com.example.petcare.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AppUserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppUserResponseDto registerUser(AppUserRequestDto dto) {
        AppUser user = new AppUser();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(true);

        AppUser savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    private AppUserResponseDto mapToDto(AppUser user) {
        return AppUserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .active(user.isActive())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}