package com.jobshield.config;

import com.jobshield.model.Company;
import com.jobshield.model.Role;
import com.jobshield.model.User;
import com.jobshield.repository.CompanyRepository;
import com.jobshield.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Seed Admin
            User admin = User.builder()
                    .email("admin@jobshield.ai")
                    .passwordHash(passwordEncoder.encode("Admin@123"))
                    .fullName("JobShield Security Director")
                    .phoneNumber("+919988776655")
                    .role(Role.ROLE_ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);

            // Seed Student Job Seeker
            User student = User.builder()
                    .email("student@university.edu")
                    .passwordHash(passwordEncoder.encode("Seeker@123"))
                    .fullName("Rohan Verma")
                    .phoneNumber("+919876543210")
                    .role(Role.ROLE_JOB_SEEKER)
                    .isActive(true)
                    .build();
            userRepository.save(student);
        }

        if (companyRepository.count() == 0) {
            companyRepository.saveAll(List.of(
                Company.builder()
                    .name("Tata Consultancy Services")
                    .normalizedName("tata consultancy services")
                    .officialDomain("tcs.com")
                    .headquarters("Mumbai, India")
                    .verificationStatus("VERIFIED_LEGITIMATE")
                    .officialCareersUrl("https://ibegin.tcs.com")
                    .knownEmailDomains("[\"@tcs.com\", \"@tataconsultancy.com\"]")
                    .build(),
                Company.builder()
                    .name("Infosys Limited")
                    .normalizedName("infosys limited")
                    .officialDomain("infosys.com")
                    .headquarters("Bengaluru, India")
                    .verificationStatus("VERIFIED_LEGITIMATE")
                    .officialCareersUrl("https://career.infosys.com")
                    .knownEmailDomains("[\"@infosys.com\"]")
                    .build(),
                Company.builder()
                    .name("Wipro Technologies")
                    .normalizedName("wipro technologies")
                    .officialDomain("wipro.com")
                    .headquarters("Bengaluru, India")
                    .verificationStatus("VERIFIED_LEGITIMATE")
                    .officialCareersUrl("https://careers.wipro.com")
                    .knownEmailDomains("[\"@wipro.com\"]")
                    .build(),
                Company.builder()
                    .name("Google India")
                    .normalizedName("google india")
                    .officialDomain("google.com")
                    .headquarters("Hyderabad, India")
                    .verificationStatus("VERIFIED_LEGITIMATE")
                    .officialCareersUrl("https://careers.google.com")
                    .knownEmailDomains("[\"@google.com\"]")
                    .build()
            ));
        }
    }
}

