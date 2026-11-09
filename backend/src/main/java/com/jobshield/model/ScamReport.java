package com.jobshield.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "scam_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScamReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reporterUserId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    private String recruiterDetails;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String incidentDescription;

    @Builder.Default
    private BigDecimal demandedAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String evidenceFilePaths;

    @Column(nullable = false)
    @Builder.Default
    private String status = "SUBMITTED";

    @Column(columnDefinition = "TEXT")
    private String adminReviewNotes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

