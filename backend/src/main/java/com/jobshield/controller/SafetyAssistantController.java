package com.jobshield.controller;

import com.jobshield.dto.ApiResponse;
import com.jobshield.dto.AppDtos;
import com.jobshield.service.GeminiSafetyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assistant")
@RequiredArgsConstructor
public class SafetyAssistantController {

    private final GeminiSafetyService geminiService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<Map<String, String>>> chat(
            @Valid @RequestBody AppDtos.AssistantChatRequest request) {
        String answer = geminiService.answerSafetyQuestion(request.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("reply", answer);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}

