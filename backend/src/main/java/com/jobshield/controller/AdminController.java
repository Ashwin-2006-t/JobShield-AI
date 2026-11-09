package com.jobshield.controller;

import com.jobshield.dto.ApiResponse;
import com.jobshield.model.AuditLog;
import com.jobshield.model.ScamReport;
import com.jobshield.repository.AuditLogRepository;
import com.jobshield.repository.JobAnalysisRepository;
import com.jobshield.repository.UserRepository;
import com.jobshield.service.ScamReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ScamReportService scamReportService;
    private final JobAnalysisRepository analysisRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalAnalyses", analysisRepository.count());
        stats.put("highRiskCount", analysisRepository.countByRiskClassification("HIGH"));
        stats.put("mediumRiskCount", analysisRepository.countByRiskClassification("MEDIUM"));
        stats.put("lowRiskCount", analysisRepository.countByRiskClassification("LOW"));
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @PatchMapping("/reports/{id}/status")
    public ResponseEntity<ApiResponse<ScamReport>> updateReportStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String adminNotes,
            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        ScamReport updated = scamReportService.updateReportStatus(id, status, adminNotes, 1L, ip);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<AuditLog> logs = auditLogRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }
}

