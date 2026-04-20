package com.example.petcare.service;

import com.example.petcare.dto.MedicalRecordRequestDto;
import com.example.petcare.entity.MedicalRecord;
import com.example.petcare.entity.Pet;
import com.example.petcare.repository.MedicalRecordRepository;
import com.example.petcare.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository recordRepository;
    private final PetRepository petRepository;

    @Transactional(readOnly = true)
    public List<MedicalRecord> getRecordsByPetId(Long petId, String userEmail) {
        Pet pet = findPetAndCheckOwnership(petId, userEmail);
        return pet.getMedicalRecords();
    }

    @Transactional
    public MedicalRecord createRecord(MedicalRecordRequestDto dto, String userEmail) {
        Pet pet = findPetAndCheckOwnership(dto.getPetId(), userEmail);

        MedicalRecord record = new MedicalRecord();
        updateRecordFields(record, dto, pet);
        return recordRepository.save(record);
    }

    @Transactional
    public MedicalRecord updateRecord(Long id, MedicalRecordRequestDto dto, String userEmail) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Záznam nenalezen"));

        // Kontrola, zda záznam patří mazlíčkovi, kterého vlastní přihlášený uživatel
        findPetAndCheckOwnership(record.getPet().getId(), userEmail);

        Pet pet = findPetAndCheckOwnership(dto.getPetId(), userEmail);
        updateRecordFields(record, dto, pet);
        return recordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(Long id, String userEmail) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Záznam nenalezen"));

        findPetAndCheckOwnership(record.getPet().getId(), userEmail);
        recordRepository.delete(record);
    }

    // Pomocná metoda pro kontrolu vlastnictví
    private Pet findPetAndCheckOwnership(Long petId, String userEmail) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mazlíček nenalezen"));
        if (!pet.getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("Nemáte oprávnění k datům tohoto mazlíčka");
        }
        return pet;
    }

    private void updateRecordFields(MedicalRecord record, MedicalRecordRequestDto dto, Pet pet) {
        record.setRecordDate(dto.getRecordDate());
        record.setType(dto.getType());
        record.setDescription(dto.getDescription());
        record.setAttachmentUrl(dto.getAttachmentUrl());
        record.setPet(pet);
    }
}