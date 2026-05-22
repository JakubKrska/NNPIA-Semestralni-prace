package com.example.petcare.repository;

import com.example.petcare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Spring Boot automaticky vygeneruje SQL dotaz: SELECT * FROM role WHERE name = ?
    Optional<Role> findByName(String name);
}