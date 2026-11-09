package com.jobshield.controller;

import com.jobshield.dto.ApiResponse;
import com.jobshield.dto.JobAnalysisDto;
import com.jobshield.model.JobAnalysis;
import com.jobshield.service.AnalysisOrchestrationService;
import com.jobshield.service.PdfReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analyze")
@RequiredArgsConstructor
public class JobAnalysisController {

    private final AnalysisOrchestrationService analysisService;
    private final PdfReportService pdfReportService;

    @PostMapping("/job")
    public ResponseEntity<ApiResponse<JobAnalysisDto.Response>> analyzeJob(
            @Valid @RequestBody JobAnalysisDto.Request request) {
        JobAnalysisDto.Response response = analysisService.analyzeJob(request, null);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/job/{id}/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        JobAnalysis analysis = analysisService.getAnalysisById(id);
        byte[] pdfBytes = pdfReportService.generatePdfReport(analysis);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=JobShield_Audit_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

