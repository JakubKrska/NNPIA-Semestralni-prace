package com.example.petcare.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "hashtag")
@Data
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(mappedBy = "hashtags")
    @EqualsAndHashCode.Exclude // Prevence (StackOverflow)
    private Set<AppUser> users;
}