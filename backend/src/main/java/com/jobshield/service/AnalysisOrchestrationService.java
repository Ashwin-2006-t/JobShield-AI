package com.jobshield.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobshield.dto.JobAnalysisDto;
import com.jobshield.model.AnalysisIndicator;
import com.jobshield.model.JobAnalysis;
import com.jobshield.model.UrlCheck;
import com.jobshield.repository.JobAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalysisOrchestrationService {

    private final HeuristicRulesEngine heuristicEngine;
    private final SafeUrlScannerService urlScanner;
    private final GeminiSafetyService geminiService;
    private final JobAnalysisRepository analysisRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public JobAnalysisDto.Response analyzeJob(JobAnalysisDto.Request request, Long userId) {
        String rawText = request.getRawText();

        // 1. Run Heuristic Rules Engine
        List<AnalysisIndicator> indicators = heuristicEngine.evaluate(rawText);

        // 2. URL Inspection (if provided or found in text)
        UrlCheck urlCheck = null;
        if (request.getSourceUrl() != null && !request.getSourceUrl().isBlank()) {
            urlCheck = urlScanner.inspectUrl(request.getSourceUrl());
            if ("MALICIOUS".equalsIgnoreCase(urlCheck.getRiskLevel())) {
                indicators.add(AnalysisIndicator.builder()
                        .category("DOMAIN_MISMATCH")
                        .title("Dangerous or Typosquatted Application URL")
                        .explanation(urlCheck.getImpersonatedTarget() != null ? urlCheck.getImpersonatedTarget() : "URL flagged as malicious")
                        .severity("CRITICAL")
                        .scoreImpact(35)
                        .build());
            }
        }

        // 3. Compute Composite Risk Score
        int heuristicScore = 0;
        List<String> indicatorTitles = new ArrayList<>();
        for (AnalysisIndicator ind : indicators) {
            heuristicScore += ind.getScoreImpact();
            indicatorTitles.add(ind.getTitle());
        }

        int finalScore = Math.max(0, Math.min(100, heuristicScore));
        String classification = finalScore >= 71 ? "HIGH" : (finalScore >= 36 ? "MEDIUM" : "LOW");

        // 4. Synthesize Gemini Explanation
        String geminiSummary = geminiService.generateRiskSummary(rawText, finalScore, indicatorTitles);

        // 5. Basic entity metadata extraction
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("lengthCharacters", rawText.length());
        metadata.put("sourceType", request.getInputSourceType());
        metadata.put("analyzedAt", new Date().toString());

        // 6. Build and persist JobAnalysis
        JobAnalysis analysis = JobAnalysis.builder()
                .userId(userId)
                .rawInputText(rawText)
                .inputSourceType(request.getInputSourceType())
                .overallRiskScore(finalScore)
                .riskClassification(classification)
                .confidenceScore(BigDecimal.valueOf(0.92))
                .extractedMetadata(metadata.toString())
                .geminiSummary(geminiSummary)
                .build();

        for (AnalysisIndicator ind : indicators) {
            ind.setJobAnalysis(analysis);
        }
        analysis.setIndicators(indicators);

        JobAnalysis saved = analysisRepository.save(analysis);

        // 7. Map to Response DTO
        List<JobAnalysisDto.IndicatorDto> indicatorDtos = new ArrayList<>();
        for (AnalysisIndicator ind : saved.getIndicators()) {
            indicatorDtos.add(JobAnalysisDto.IndicatorDto.builder()
                    .id(ind.getId())
                    .category(ind.getCategory())
                    .title(ind.getTitle())
                    .explanation(ind.getExplanation())
                    .severity(ind.getSeverity())
                    .scoreImpact(ind.getScoreImpact())
                    .build());
        }

        return JobAnalysisDto.Response.builder()
                .analysisId(saved.getId())
                .overallRiskScore(saved.getOverallRiskScore())
                .riskClassification(saved.getRiskClassification())
                .confidenceScore(saved.getConfidenceScore())
                .extractedMetadata(metadata)
                .indicators(indicatorDtos)
                .geminiSummary(saved.getGeminiSummary())
                .createdAt(saved.getCreatedAt().toString())
                .build();
    }

    public JobAnalysis getAnalysisById(Long id) {
        return analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found with ID: " + id));
    }
}

