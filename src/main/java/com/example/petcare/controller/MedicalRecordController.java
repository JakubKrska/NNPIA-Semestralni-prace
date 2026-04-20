package com.example.petcare.controller;

import com.example.petcare.dto.MedicalRecordRequestDto;
import com.example.petcare.entity.MedicalRecord;
import com.example.petcare.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService recordService;

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsForPet(@PathVariable Long petId, Authentication auth) {
        return ResponseEntity.ok(recordService.getRecordsByPetId(petId, auth.getName()));
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createRecord(@Valid @RequestBody MedicalRecordRequestDto dto, Authentication auth) {
        return ResponseEntity.status(201).body(recordService.createRecord(dto, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequestDto dto,
            Authentication auth) {
        return ResponseEntity.ok(recordService.updateRecord(id, dto, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id, Authentication auth) {
        recordService.deleteRecord(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}