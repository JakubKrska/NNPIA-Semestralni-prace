package com.example.petcare.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pet")
@Data
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords;
}