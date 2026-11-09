package com.jobshield.repository;

import com.jobshield.model.JobAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAnalysisRepository extends JpaRepository<JobAnalysis, Long> {
    List<JobAnalysis> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<JobAnalysis> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByRiskClassification(String riskClassification);
}

