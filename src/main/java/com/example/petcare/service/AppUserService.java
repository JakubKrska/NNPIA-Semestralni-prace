package com.example.petcare.service;

import com.example.petcare.dto.AppUserRequestDto;
import com.example.petcare.dto.AppUserResponseDto;
import com.example.petcare.entity.AppUser;
import com.example.petcare.repository.AppUserRepository;
import com.example.petcare.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AppUserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppUserResponseDto registerUser(AppUserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Uživatel s tímto e-mailem již existuje.");
        }

        AppUser user = new AppUser();
        user.setEmail(dto.getEmail());
        // BCrypt automaticky generuje náhodnou sůl (Salt) a přidává ji přímo do výsledného řetězce.
        // Tím brání útokům typu Rainbow Tables.
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(true);

        com.example.petcare.entity.Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    com.example.petcare.entity.Role newRole = new com.example.petcare.entity.Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });


        user.setRoles(Set.of(userRole));

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