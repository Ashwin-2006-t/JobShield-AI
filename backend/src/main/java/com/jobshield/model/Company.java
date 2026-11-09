package com.jobshield.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String normalizedName;

    @Column(nullable = false, unique = true)
    private String officialDomain;

    private String headquarters;

    @Column(nullable = false)
    @Builder.Default
    private String verificationStatus = "UNVERIFIED";

    private String officialCareersUrl;

    @Column(columnDefinition = "TEXT")
    private String knownEmailDomains;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

