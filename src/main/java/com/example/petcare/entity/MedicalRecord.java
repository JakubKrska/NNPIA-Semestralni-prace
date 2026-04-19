package com.example.petcare.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_record")
@Data
@NoArgsConstructor
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime recordDate;
    private String type; // očkování, kontrola atd..

    @Column(columnDefinition = "TEXT")
    private String description;

    private String attachmentUrl;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}