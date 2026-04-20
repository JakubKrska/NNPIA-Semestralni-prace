package com.example.petcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecordRequestDto {
    @NotNull(message = "Datum záznamu je povinné")
    private LocalDateTime recordDate;

    @NotBlank(message = "Typ záznamu je povinný")
    private String type;

    private String description;
    private String attachmentUrl;

    @NotNull(message = "ID mazlíčka je povinné")
    private Long petId;
}