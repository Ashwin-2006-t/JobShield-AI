package com.jobshield.controller;

import com.jobshield.dto.ApiResponse;
import com.jobshield.dto.AppDtos;
import com.jobshield.model.UrlCheck;
import com.jobshield.service.SafeUrlScannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urlscan")
@RequiredArgsConstructor
public class UrlScanController {

    private final SafeUrlScannerService urlScannerService;

    @PostMapping("/inspect")
    public ResponseEntity<ApiResponse<UrlCheck>> inspectUrl(@Valid @RequestBody AppDtos.UrlScanRequest request) {
        UrlCheck result = urlScannerService.inspectUrl(request.getUrl());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}

