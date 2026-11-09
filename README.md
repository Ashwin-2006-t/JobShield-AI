# JobShield AI — AI-Powered Fake Job Posting Detection and Recruitment Verification Platform

[![Final Year CSE Project](https://img.shields.io/badge/Project-Final%20Year%20CSE-10B981?style=for-the-badge)](https://github.com)
[![SDG 8 Aligned](https://img.shields.io/badge/UN%20SDG-8%20Decent%20Work-059669?style=for-the-badge)](https://sdgs.un.org/goals/goal8)
[![Architecture](https://img.shields.io/badge/Architecture-Modular%20Monolith-blue?style=for-the-badge)](docs/)
[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203.3-brightgreen?style=for-the-badge)](backend/)
[![FastAPI](https://img.shields.io/badge/ML%20Service-FastAPI%20%2B%20Scikit--Learn-yellowgreen?style=for-the-badge)](ml-service/)
[![React](https://img.shields.io/badge/Frontend-React%2018%20%2B%20Tailwind-61DAFB?style=for-the-badge)](web/)
[![React Native](https://img.shields.io/badge/Mobile-React%20Native%20(Expo)-9945FF?style=for-the-badge)](mobile/)

---

## 1. Project Overview & Problem Statement
With the exponential rise in digital recruitment scams, students and fresh college graduates are increasingly exploited through fraudulent job postings demanding upfront "laptop security deposits", "training fees", or sharing sensitive identity documents (Aadhaar, bank account details).

**JobShield AI** is an explainable cybersecurity decision-support platform designed to protect job seekers before they apply, pay extortion fees, or surrender private identity data.

### Key Capabilities:
1. **Single-Textarea NLP Analyzer**: Paste unformatted job advertisements from LinkedIn, WhatsApp, or Email; the system automatically extracts entities, compensation, contacts, and fees.
2. **Hybrid Decision Engine**: Combines **15 deterministic heuristic rules** for absolute red flags with **TF-IDF + Calibrated statistical machine learning** for linguistic nuance.
3. **Explainable AI (XAI) Token Attribution**: Visually highlights suspicious and trusted phrases directly inside the user's input with color-coded risk weights.
4. **SSRF-Safe URL Inspector**: Analyzes job application links, de-obfuscates URL shorteners, and blocks Server-Side Request Forgery attempts against private subnets.
5. **Company Domain Authenticity Engine**: Detects domain mismatch between enterprise brand claims (e.g., TCS, Infosys) and unverified free webmail addresses (`@gmail.com`).
6. **Community Scam Reporting & Admin Moderation**: Complete community alert feed with admin review workflows and audit logging.
7. **AI Recruitment Safety Counselor**: Natural language chatbot grounded in **Indian National Cyber Crime Portal (1930 Helpline)**, **CERT-In**, and **National Career Service (NCS)** guidelines.
8. **Forensic Audit PDF Exporter**: Instant client-side and server-side PDF audit report generation.

---

## 2. UN Sustainable Development Goal (SDG) Alignment
- **SDG 8: Decent Work and Economic Growth (Target 8.5 & 8.8)**: Directly protects vulnerable job seekers from illegal recruitment fees and digital extortion.
- **SDG 16: Peace, Justice, and Strong Institutions (Target 16.5 & 16.6)**: Reduces financial cybercrime flows through auditable evidence trails submitted to cybercrime authorities.
- **SDG 10: Reduced Inequalities (Target 10.2 & 10.3)**: Shields economically disadvantaged and first-generation college students targeted by predatory employment syndicates.

---

## 3. Monorepo Structure

```
JobSentry/
├── backend/                  # Java 17/21 Spring Boot 3.3.0 Modular Monolith
│   ├── src/main/java/com/jobshield/
│   │   ├── config/           # SecurityConfig, JWT Provider & Filter, DatabaseSeeder
│   │   ├── controller/       # Auth, Analyze, URLScan, Company, Reports, Admin, Assistant
│   │   ├── dto/              # Unified API Envelopes, JobAnalysisDto, AuthDto
│   │   ├── model/            # User, Company, JobAnalysis, Indicator, UrlCheck, ScamReport, AuditLog
│   │   ├── repository/       # Spring Data JPA Repositories
│   │   └── service/          # HeuristicRulesEngine, SafeUrlScanner, Orchestrator, PdfService
│   ├── src/main/resources/   # application.yml, schema.sql
│   └── pom.xml
├── ml-service/               # Python 3.10+ FastAPI AI/ML Microservice
│   ├── app/
│   │   ├── dataset/          # seed_jobs.json (curated test records)
│   │   ├── pipeline/         # extractor.py, classifier.py, xai_explainer.py
│   │   └── main.py           # FastAPI REST API & internal auth guard
│   ├── requirements.txt
│   └── Dockerfile
├── web/                      # React 18 + Vite + Tailwind CSS Web Application
│   ├── index.html            # Standalone, zero-dependency browser-executable SPA
│   └── package.json
├── mobile/                   # React Native (Expo SDK 51) Native Application
│   ├── App.tsx               # Full native mobile screens matching web design tokens
│   ├── app.json
│   └── package.json
├── shared/                   # Shared TypeScript contracts & Risk Constants
│   ├── types/index.ts
│   └── constants/index.ts
├── run.bat                   # 1-Click Windows Launcher (Opens in Chrome immediately)
├── start-server.ps1          # Built-in PowerShell Local HTTP Server (http://localhost:3000)
└── docker-compose.yml        # Multi-container orchestration (MySQL, Spring Boot, FastAPI, Web)
```

---

## 4. How to Run & Demonstrate JobShield AI

### Method 1: Instant 1-Click Browser Launch (Recommended for Live Demo)
Simply double-click the included `run.bat` file in this folder, or run:
```powershell
start "web/index.html"
```
This immediately opens the fully functional **JobShield AI Cybersecurity Platform** in Google Chrome or your default browser with zero dependencies required!

### Method 2: Local HTTP Server (PowerShell)
To run on `http://localhost:3000`:
```powershell
powershell -ExecutionPolicy Bypass -File .\start-server.ps1
```

### Method 3: Full Enterprise Stack (Docker Compose)
To start MySQL 8, the Spring Boot Backend, and the FastAPI ML Microservice together:
```bash
docker-compose up --build
```

---

## 5. Live Demonstration Guide for Examiners

1. **Test Case 1 (High-Risk Scam Demonstration)**:
   - Click **"Demo Scam (TCS Impersonation)"** above the input box.
   - Click **"RUN RISK & AUTHENTICITY ANALYSIS"**.
   - Observe:
     - Radar scanning animation simulating entity and domain audit.
     - **Risk Score Gauge** jumps to **88/100 (HIGH RISK SCAM)** in crimson red.
     - **XAI Token Attribution** highlights *"refundable registration and laptop courier security fee of Rs 1,500"* and *"hr.tcsrecruitment2026@gmail.com"* with risk coefficient tooltips.
     - Warning signs table details the violations (+45 for upfront fee, +35 for brand webmail).
     - Click **"Download PDF Audit Report"** to export a formatted forensic report.

2. **Test Case 2 (Legitimate MNC Clearance)**:
   - Click **"Demo Legit (Infosys Official)"**.
   - Click **"RUN RISK & AUTHENTICITY ANALYSIS"**.
   - Observe:
     - Gauge shows **10/100 (LOW RISK)** in emerald green.
     - Verified trust indicators reward the explicit anti-fee policy statement.

3. **Test Case 3 (SSRF Attack Prevention & URL Inspector)**:
   - Switch to the **"URL Inspector"** tab.
   - Test `http://127.0.0.1:8080/admin` or `http://169.254.169.254`.
   - Observe the **SSRF Attempt Blocked** notice, demonstrating backend network security.

4. **Test Case 4 (AI Recruitment Safety Counselor)**:
   - Switch to the **"Safety Counselor"** tab.
   - Click the prompt chip: *"Is it normal to pay Rs 2000 for laptop courier?"*
   - Observe the immediate grounded response referencing Indian Ministry of Labour laws and the 1930 Cybercrime Helpline.

5. **Test Case 5 (Admin Portal & Audit Logs)**:
   - Switch to the **"Admin Portal"** tab.
   - View telemetry stats, platform risk distribution bar chart, report moderation queue, and the chronological security audit trail.

