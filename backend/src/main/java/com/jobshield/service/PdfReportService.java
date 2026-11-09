package com.jobshield.service;

import com.jobshield.model.AnalysisIndicator;
import com.jobshield.model.JobAnalysis;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfReportService {

    public byte[] generatePdfReport(JobAnalysis analysis) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 40, 40);
            PdfWriter.getInstance(document, out);
            document.open();

            // Header Title
            Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(16, 185, 129));
            Paragraph header = new Paragraph("JOBSHIELD AI - RECRUITMENT FORENSIC AUDIT", brandFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Paragraph sub = new Paragraph("AI-Powered Job Authenticity & Cyber Safety Verification Platform\n\n", subFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            document.add(sub);

            // Audit Summary Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(15f);

            PdfPCell cell1 = new PdfPCell(new Phrase("Audit ID: #" + analysis.getId()));
            PdfPCell cell2 = new PdfPCell(new Phrase("Generated: " + analysis.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell1);
            table.addCell(cell2);
            document.add(table);

            // Risk Banner
            Color riskColor = analysis.getOverallRiskScore() >= 70 ? new Color(239, 68, 68) :
                             (analysis.getOverallRiskScore() >= 36 ? new Color(245, 158, 11) : new Color(16, 185, 129));
            Font scoreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE);
            PdfPTable banner = new PdfPTable(1);
            banner.setWidthPercentage(100);
            PdfPCell bannerCell = new PdfPCell(new Phrase("OVERALL RISK SCORE: " + analysis.getOverallRiskScore() + "/100 (" + analysis.getRiskClassification() + " RISK)", scoreFont));
            bannerCell.setBackgroundColor(riskColor);
            bannerCell.setPadding(12f);
            bannerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            banner.addCell(bannerCell);
            document.add(banner);

            // Gemini Summary
            document.add(new Paragraph("\nExecutive Assessment Summary:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            Paragraph summaryPara = new Paragraph(analysis.getGeminiSummary() != null ? analysis.getGeminiSummary() : "Analysis completed.", FontFactory.getFont(FontFactory.HELVETICA, 10));
            document.add(summaryPara);

            // Indicators Table
            document.add(new Paragraph("\nDetected Risk Indicators & Evidence:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            PdfPTable indTable = new PdfPTable(3);
            indTable.setWidthPercentage(100);
            indTable.setWidths(new float[]{2f, 5f, 1.5f});
            indTable.setSpacingBefore(8f);

            indTable.addCell(new Phrase("Severity", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            indTable.addCell(new Phrase("Indicator Title & Details", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            indTable.addCell(new Phrase("Risk Impact", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));

            for (AnalysisIndicator ind : analysis.getIndicators()) {
                indTable.addCell(new Phrase(ind.getSeverity(), FontFactory.getFont(FontFactory.HELVETICA, 9)));
                indTable.addCell(new Phrase(ind.getTitle() + "\n" + ind.getExplanation(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                indTable.addCell(new Phrase((ind.getScoreImpact() > 0 ? "+" : "") + ind.getScoreImpact(), FontFactory.getFont(FontFactory.HELVETICA, 9)));
            }
            document.add(indTable);

            // Legal & Safety Footer
            document.add(new Paragraph("\nOfficial Reporting Guidance:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            document.add(new Paragraph("National Cyber Crime Helpline: 1930 | Official Reporting Portal: https://cybercrime.gov.in\n" +
                    "National Career Service (Ministry of Labour & Employment): https://ncs.gov.in\n" +
                    "This report is an automated decision-support indicator provided for personal safety verification.", FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY)));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF audit report", e);
        }
    }
}

