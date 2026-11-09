package com.jobshield.repository;

import com.jobshield.model.ScamReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScamReportRepository extends JpaRepository<ScamReport, Long> {
    List<ScamReport> findByReporterUserIdOrderByCreatedAtDesc(Long reporterUserId);
    Page<ScamReport> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByStatus(String status);
}

