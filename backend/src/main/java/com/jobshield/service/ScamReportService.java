package com.jobshield.service;

import com.jobshield.dto.AppDtos;
import com.jobshield.model.AuditLog;
import com.jobshield.model.ScamReport;
import com.jobshield.repository.AuditLogRepository;
import com.jobshield.repository.ScamReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScamReportService {

    private final ScamReportRepository scamReportRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public ScamReport createReport(AppDtos.ScamReportRequest request, Long userId) {
        ScamReport report = ScamReport.builder()
                .reporterUserId(userId)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .recruiterDetails(request.getRecruiterContact())
                .incidentDescription(request.getIncidentDescription())
                .demandedAmount(request.getDemandedAmount())
                .status("SUBMITTED")
                .build();
        return scamReportRepository.save(report);
    }

    public List<ScamReport> getReportsByUser(Long userId) {
        return scamReportRepository.findByReporterUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<ScamReport> getAllReports(Pageable pageable) {
        return scamReportRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public ScamReport updateReportStatus(Long id, String status, String adminNotes, Long adminId, String ipAddress) {
        ScamReport report = scamReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scam report not found with id: " + id));

        report.setStatus(status);
        report.setAdminReviewNotes(adminNotes);
        ScamReport saved = scamReportRepository.save(report);

        // Record Audit Log
        AuditLog audit = AuditLog.builder()
                .adminUserId(adminId)
                .actionType("UPDATE_SCAM_REPORT_STATUS")
                .targetEntity("ScamReport")
                .targetId(id)
                .details("Status updated to: " + status + ". Notes: " + adminNotes)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(audit);

        return saved;
    }
}

