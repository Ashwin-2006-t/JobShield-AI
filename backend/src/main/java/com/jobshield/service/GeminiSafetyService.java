package com.jobshield.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiSafetyService {

    @Value("${app.services.gemini.api-key:demo_key}")
    private String geminiApiKey;

    public String generateRiskSummary(String jobText, int riskScore, List<String> indicators) {
        if (riskScore >= 70) {
            return "CRITICAL ALERT: This job advertisement exhibits multiple high-confidence indicators of recruitment fraud. " +
                   "Under Indian Ministry of Labour and ILO standards, charging applicants any registration, laptop, or training fee is illegal. " +
                   "Do not transfer funds or share identity documents. Report suspicious contacts to the National Cyber Crime Helpline at 1930.";
        } else if (riskScore >= 36) {
            return "MODERATE WARNING: Several suspicious elements were flagged in this posting, such as generic contact details or unverified interview procedures. " +
                   "Cross-check this requirement directly on the organization's official careers portal before responding.";
        } else {
            return "VERIFIED CLEARANCE: This recruitment notice adheres to standard corporate hiring protocols, including formal assessment rounds and zero-fee compliance. " +
                   "Always ensure your communications occur through authenticated enterprise domain emails.";
        }
    }

    public String answerSafetyQuestion(String question) {
        String q = question.toLowerCase();

        if (q.contains("fee") || q.contains("money") || q.contains("deposit") || q.contains("pay") || q.contains("laptop")) {
            return "NO, NEVER PAY. Legitimate employers never ask candidates to pay for interviews, registration, laptop delivery, training, or uniform charges. " +
                   "Under Indian Labour Ministry regulations, charging candidates is strictly prohibited. If someone claims to be from TCS, Infosys, or Amazon and asks for a UPI transfer, it is 100% a scam.";
        } else if (q.contains("already paid") || q.contains("cheated") || q.contains("lost money")) {
            return "IMMEDIATE STEPS:\n" +
                   "1. Call 1930 (National Cyber Crime Reporting Helpline) immediately to freeze fraudulent bank accounts/UPI IDs.\n" +
                   "2. Lodge a formal digital complaint at https://cybercrime.gov.in.\n" +
                   "3. Contact your bank or payment app (GPay/PhonePe/Paytm) within 24 hours to dispute the transaction.";
        } else if (q.contains("verify") || q.contains("offer letter") || q.contains("genuine")) {
            return "HOW TO VERIFY AN OFFER LETTER:\n" +
                   "1. Check the sender email domain: Genuine recruiters write from @tcs.com, @infosys.com, etc., NEVER from @gmail.com or @yahoo.com.\n" +
                   "2. Verify the Candidate/Job Reference ID directly on the company's official career portal.\n" +
                   "3. Legitimate offers are never sent without structured technical and HR interviews.";
        } else {
            return "Always protect your personal identity. Never share your Aadhaar OTP, bank account PIN, or blank cheques with anyone claiming to be a recruiter. " +
                   "Verify government job vacancies on the National Career Service portal (ncs.gov.in) and report cyber scams to 1930.";
        }
    }
}

