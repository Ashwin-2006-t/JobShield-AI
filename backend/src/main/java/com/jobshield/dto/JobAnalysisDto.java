package com.jobshield.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class JobAnalysisDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String rawText;
        private String sourceUrl;
        @Builder.Default
        private String inputSourceType = "TEXT_PASTE";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IndicatorDto {
        private Long id;
        private String category;
        private String title;
        private String explanation;
        private String severity;
        private Integer scoreImpact;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long analysisId;
        private Integer overallRiskScore;
        private String riskClassification;
        private BigDecimal confidenceScore;
        private Map<String, Object> extractedMetadata;
        private List<IndicatorDto> indicators;
        private List<Map<String, Object>> xaiTokenHighlights;
        private Map<String, Object> urlAnalysis;
        private String geminiSummary;
        private String createdAt;
    }
}

