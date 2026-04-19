package com.example.petcare.controller;

import com.example.petcare.entity.MedicalRecord;
import com.example.petcare.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor // Generuje konstruktor pro final pole
public class MedicalRecordController {

    private final MedicalRecordRepository recordRepository;

    @GetMapping
    public List<MedicalRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    @PostMapping
    public MedicalRecord createRecord(@RequestBody MedicalRecord record) {
        return recordRepository.save(record);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long id) {
        return recordRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateRecord(@PathVariable Long id, @RequestBody MedicalRecord recordDetails) {
        return recordRepository.findById(id)
                .map(record -> {
                    record.setRecordDate(recordDetails.getRecordDate());
                    record.setType(recordDetails.getType());
                    record.setDescription(recordDetails.getDescription());
                    record.setAttachmentUrl(recordDetails.getAttachmentUrl());
                    return ResponseEntity.ok(recordRepository.save(record));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (recordRepository.existsById(id)) {
            recordRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}