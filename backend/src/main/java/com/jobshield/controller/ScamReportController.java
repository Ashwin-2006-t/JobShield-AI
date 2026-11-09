package com.jobshield.controller;

import com.jobshield.dto.ApiResponse;
import com.jobshield.dto.AppDtos;
import com.jobshield.model.ScamReport;
import com.jobshield.service.ScamReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ScamReportController {

    private final ScamReportService scamReportService;

    @PostMapping
    public ResponseEntity<ApiResponse<ScamReport>> createReport(
            @Valid @RequestBody AppDtos.ScamReportRequest request) {
        ScamReport report = scamReportService.createReport(request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(report));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ScamReport>>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ScamReport> reports = scamReportService.getAllReports(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(reports));
    }
}

