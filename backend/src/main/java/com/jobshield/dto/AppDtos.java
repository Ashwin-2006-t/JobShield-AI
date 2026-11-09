package com.jobshield.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

public class AppDtos {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UrlScanRequest {
        @NotBlank
        private String url;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScamReportRequest {
        @NotBlank
        private String companyName;
        @NotBlank
        private String jobTitle;
        private String recruiterContact;
        @NotBlank
        private String incidentDescription;
        private BigDecimal demandedAmount;
        private List<String> evidenceUrls;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AssistantChatRequest {
        @NotBlank
        private String message;
        private List<ChatMessage> history;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessage {
        private String role;
        private String content;
    }
}

