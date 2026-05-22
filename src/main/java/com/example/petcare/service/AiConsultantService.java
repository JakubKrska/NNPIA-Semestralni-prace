package com.example.petcare.service;

import com.example.petcare.entity.Pet;
import com.example.petcare.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiConsultantService {

    private final PetRepository petRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String askAiConsultant(Long petId, String userQuestion, String userEmail) {
        //Validace a načtení mazlíčka (Bezpečnost: přístup pouze k vlastním datům)
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mazlíček nenalezen"));

        if (!pet.getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("Nemáte oprávnění k datům tohoto mazlíčka");
        }

        //Sestavení zdravotní historie do přehledného kontextu
        String historyContext = pet.getMedicalRecords().stream()
                .map(record -> String.format("- %s (%s): %s",
                        record.getRecordDate().toLocalDate(), record.getType(), record.getDescription()))
                .collect(Collectors.joining("\n"));

        //Prompt engineering: Definice chování AI asistenta v češtině
        String systemPrompt = String.format(
                "Jsi zkušený veterinární asistent v aplikaci PetCare. Tvým úkolem je empaticky a odborně poradit majiteli.\n" +
                        "Údaje o zvířeti: Jméno: %s, Druh: %s, Plemeno: %s, Váha: %.1f kg.\n" +
                        "Předchozí lékařské záznamy zvířete:\n%s\n\n" +
                        "STRIKTNÍ PRAVIDLO: Odpovídej v jazyce dotazu (česky). Pokud symptomy naznačují akutní stav (netečnost, zvracení krve, kolaps, dýchavičnost), " +
                        "důrazně doporuč okamžitou návštěvu veterináře. Tvůj výstup nenahrazuje odbornou péči.",
                pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getWeight(),
                historyContext.isEmpty() ? "Žádné předchozí záznamy." : historyContext
        );

        //Příprava JSON těla požadavku pro Groq API
        Map<String, Object> requestBody = new HashMap<>();
        // Používáme  open-source model Llama 3.3 (70B versatile)
        requestBody.put("model", "llama-3.3-70b-versatile");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userQuestion));
        requestBody.put("messages", messages);

        //HTTP Hlavičky s Bearer Tokenem (Authorization)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            //Odeslání požadavku na Groq Cloud
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            //Rozbalení odpovědi
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            throw new RuntimeException("Chyba při komunikaci s Groq AI: " + e.getMessage());
        }
    }
}