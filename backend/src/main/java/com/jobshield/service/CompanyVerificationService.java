package com.jobshield.service;

import com.jobshield.model.Company;
import com.jobshield.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyVerificationService {

    private final CompanyRepository companyRepository;

    public List<Company> searchCompanies(String query) {
        if (query == null || query.isBlank()) {
            return companyRepository.findAll();
        }
        return companyRepository.searchCompanies(query.trim());
    }

    public Optional<Company> findByDomain(String domain) {
        return companyRepository.findByOfficialDomain(domain.toLowerCase());
    }

    public boolean isAuthorizedEmailDomain(String companyName, String email) {
        if (email == null || !email.contains("@")) return false;
        String emailDomain = email.substring(email.indexOf("@")).toLowerCase();

        List<Company> matches = companyRepository.searchCompanies(companyName);
        if (matches.isEmpty()) return true; // Cannot verify unknown company

        Company company = matches.get(0);
        String known = company.getKnownEmailDomains();
        return known != null && known.toLowerCase().contains(emailDomain);
    }
}

