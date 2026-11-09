package com.jobshield.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "url_checks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long analysisId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resolvedFinalUrl;

    @Column(nullable = false)
    private String domain;

    @Builder.Default
    private Boolean isShortener = false;

    @Builder.Default
    private Boolean hasValidSsl = false;

    @Builder.Default
    private Boolean isTyposquatting = false;

    private String impersonatedTarget;

    @Column(nullable = false)
    private String riskLevel;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

