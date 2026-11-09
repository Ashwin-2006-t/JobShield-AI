package com.jobshield.controller;

import com.jobshield.dto.ApiResponse;
import com.jobshield.model.Company;
import com.jobshield.service.CompanyVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyVerificationService companyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Company>>> searchCompanies(
            @RequestParam(required = false) String query) {
        List<Company> list = companyService.searchCompanies(query);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}

