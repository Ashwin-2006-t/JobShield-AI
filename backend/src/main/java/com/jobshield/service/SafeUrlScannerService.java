package com.jobshield.service;

import com.jobshield.model.UrlCheck;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

@Service
public class SafeUrlScannerService {

    private static final Set<String> SHORTENERS = Set.of(
            "bit.ly", "tinyurl.com", "t.co", "cutt.ly", "is.gd", "rb.gy", "rebrand.ly"
    );

    private static final List<String> PROTECTED_BRANDS = List.of(
            "tcs.com", "infosys.com", "wipro.com", "amazon.jobs", "google.com", "microsoft.com"
    );

    public UrlCheck inspectUrl(String urlString) {
        UrlCheck.UrlCheckBuilder builder = UrlCheck.builder()
                .inputUrl(urlString)
                .resolvedFinalUrl(urlString)
                .riskLevel("SAFE");

        try {
            URI uri = new URI(urlString);
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                return builder.domain("invalid")
                        .riskLevel("MALICIOUS")
                        .impersonatedTarget("Invalid or non-HTTP URI scheme")
                        .build();
            }

            builder.hasValidSsl(scheme.equalsIgnoreCase("https"));
            String host = uri.getHost();
            if (host == null) {
                return builder.domain("invalid").riskLevel("MALICIOUS").build();
            }
            builder.domain(host);

            // 1. SSRF Protection - Disallow private / loopback IP resolution
            try {
                InetAddress[] addresses = InetAddress.getAllByName(host);
                for (InetAddress addr : addresses) {
                    if (addr.isLoopbackAddress() || addr.isSiteLocalAddress() || addr.isLinkLocalAddress() || addr.isAnyLocalAddress()) {
                        return builder.riskLevel("MALICIOUS")
                                .impersonatedTarget("SSRF Attempt: Points to private/internal network IP " + addr.getHostAddress())
                                .build();
                    }
                }
            } catch (UnknownHostException e) {
                return builder.riskLevel("SUSPICIOUS")
                        .impersonatedTarget("Unresolvable domain name")
                        .build();
            }

            // 2. Shortener Detection
            if (SHORTENERS.contains(host.toLowerCase())) {
                builder.isShortener(true);
                builder.riskLevel("SUSPICIOUS");
            }

            // 3. Typosquatting / Homoglyph Check
            for (String brand : PROTECTED_BRANDS) {
                int dist = levenshteinDistance(host.toLowerCase(), brand);
                if (dist > 0 && dist <= 3) {
                    builder.isTyposquatting(true);
                    builder.riskLevel("MALICIOUS");
                    builder.impersonatedTarget("Suspected typosquatting attack mimicking " + brand);
                    break;
                }
            }

        } catch (Exception e) {
            return builder.domain("malformed")
                    .riskLevel("MALICIOUS")
                    .impersonatedTarget("Malformed URL syntax")
                    .build();
        }

        return builder.build();
    }

    private int levenshteinDistance(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}

