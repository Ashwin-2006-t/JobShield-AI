package com.jobshield.service;

import com.jobshield.model.AnalysisIndicator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class HeuristicRulesEngine {

    private static final Pattern UPFRONT_FEE_PATTERN = Pattern.compile(
            "(?i)(registration|processing|security|uniform|laptop|training|server\\s*activation)\\s*(fee|deposit|charge|amount|payment|cost)");

    private static final Pattern INSTANT_OFFER_PATTERN = Pattern.compile(
            "(?i)(no\\s*interview|direct\\s*selection|spot\\s*offer|100%\\s*placement|immediate\\s*joining\\s*letter|direct\\s*appointment)");

    private static final Pattern TELEGRAM_WHATSAPP_PATTERN = Pattern.compile(
            "(?i)(interview\\s*(via|on|through)\\s*(telegram|whatsapp)|contact\\s*hr\\s*on\\s*telegram|join\\s*telegram\\s*channel)");

    private static final Pattern UPI_PAYMENT_PATTERN = Pattern.compile(
            "(?i)(send\\s*(money|cash|fee)\\s*to\\s*(upi|gpay|phonepe|paytm)|upi\\s*scanner|transfer\\s*to\\s*account)");

    private static final Pattern FREE_WEBMAIL_PATTERN = Pattern.compile(
            "(?i)@[a-zA-Z0-9-.]*(gmail|yahoo|hotmail|outlook|rediffmail)\\.com");

    private static final Pattern TRUST_ZERO_FEE_PATTERN = Pattern.compile(
            "(?i)(never\\s*(asks\\s*for|charges)\\s*(any\\s*)?fees?|does\\s*not\\s*charge\\s*any\\s*fee|recruitment\\s*is\\s*free)");

    public List<AnalysisIndicator> evaluate(String text) {
        List<AnalysisIndicator> indicators = new ArrayList<>();

        // Check 1: Upfront Fee
        if (UPFRONT_FEE_PATTERN.matcher(text).find()) {
            indicators.add(AnalysisIndicator.builder()
                    .category("FINANCIAL_EXTORTION")
                    .title("Upfront Fee / Security Deposit Demanded")
                    .explanation("The posting requests money for registration, laptop delivery, or training. Legitimate employers never charge candidates.")
                    .severity("CRITICAL")
                    .scoreImpact(40)
                    .build());
        }

        // Check 2: Instant Offer / No Interview
        if (INSTANT_OFFER_PATTERN.matcher(text).find()) {
            indicators.add(AnalysisIndicator.builder()
                    .category("UNREALISTIC_OFFER")
                    .title("Instant Appointment Without Technical Assessment")
                    .explanation("Claims of immediate selection without standard interview rounds indicate predatory recruitment scams.")
                    .severity("CRITICAL")
                    .scoreImpact(30)
                    .build());
        }

        // Check 3: Telegram / WhatsApp Exclusive Communication
        if (TELEGRAM_WHATSAPP_PATTERN.matcher(text).find()) {
            indicators.add(AnalysisIndicator.builder()
                    .category("COMMUNICATION_RISK")
                    .title("Unregulated Chat-Only Interview Channel")
                    .explanation("Conducting recruitment interviews exclusively through Telegram or WhatsApp prevents verification and facilitates identity fraud.")
                    .severity("WARNING")
                    .scoreImpact(25)
                    .build());
        }

        // Check 4: Payment Handles / UPI Mentioned
        if (UPI_PAYMENT_PATTERN.matcher(text).find()) {
            indicators.add(AnalysisIndicator.builder()
                    .category("FINANCIAL_EXTORTION")
                    .title("Personal Payment App / UPI Transfer Mentioned")
                    .explanation("Instructions to transfer money via GPay, PhonePe, or UPI scanners are definitive hallmarks of job scam extortion.")
                    .severity("CRITICAL")
                    .scoreImpact(35)
                    .build());
        }

        // Check 5: Brand Impersonation using Free Webmail
        boolean hasFreeWebmail = FREE_WEBMAIL_PATTERN.matcher(text).find();
        boolean hasMncBrand = Pattern.compile("(?i)(tcs|infosys|wipro|amazon|google|microsoft|cognizant|accenture)").matcher(text).find();
        if (hasFreeWebmail && hasMncBrand) {
            indicators.add(AnalysisIndicator.builder()
                    .category("DOMAIN_MISMATCH")
                    .title("Free Public Webmail Used For Enterprise Brand")
                    .explanation("The posting claims to represent an enterprise multinational but uses an unverified public webmail (Gmail/Yahoo) instead of corporate domain.")
                    .severity("CRITICAL")
                    .scoreImpact(35)
                    .build());
        }

        // Check 6: Positive Trust - Zero Fee Declaration
        if (TRUST_ZERO_FEE_PATTERN.matcher(text).find()) {
            indicators.add(AnalysisIndicator.builder()
                    .category("POSITIVE_TRUST")
                    .title("Explicit Anti-Fee Compliance Policy Stated")
                    .explanation("Posting contains formal employer statement that no recruitment charges or security deposits are ever collected.")
                    .severity("POSITIVE")
                    .scoreImpact(-25)
                    .build());
        }

        return indicators;
    }
}

