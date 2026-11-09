package com.jobshield.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_analyses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String rawInputText;

    @Column(nullable = false)
    @Builder.Default
    private String inputSourceType = "TEXT_PASTE";

    @Column(nullable = false)
    private Integer overallRiskScore;

    @Column(nullable = false)
    private String riskClassification;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal confidenceScore;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String extractedMetadata;

    @Column(columnDefinition = "TEXT")
    private String xaiTokenHighlights;

    @Column(columnDefinition = "TEXT")
    private String geminiSummary;

    private String pdfReportStoragePath;

    @OneToMany(mappedBy = "jobAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AnalysisIndicator> indicators = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

