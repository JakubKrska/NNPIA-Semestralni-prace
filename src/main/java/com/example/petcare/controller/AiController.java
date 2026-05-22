package com.example.petcare.controller;

import com.example.petcare.service.AiConsultantService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiConsultantService aiService;

    @PostMapping("/consult")
    public ResponseEntity<?> askAi(@RequestBody AiRequest request, Authentication authentication) {

        if (request.getPetId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Chyba: Backend přijal petId jako NULL. Zkontroluj frontend payload."));
        }

        if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Chyba: Otázka nesmí být prázdná."));
        }

        try {
            String userEmail = authentication.getName();
            String aiResponse = aiService.askAiConsultant(request.getPetId(), request.getQuestion(), userEmail);

            return ResponseEntity.ok(Map.of("answer", aiResponse));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Došlo k chybě při komunikaci s AI: " + e.getMessage()));
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AiRequest {
        private Long petId;
        private String question;
    }
}