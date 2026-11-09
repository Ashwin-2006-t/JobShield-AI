-- JobShield AI Relational Database Initialization
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone_number VARCHAR(30) NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_JOB_SEEKER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    normalized_name VARCHAR(200) NOT NULL,
    official_domain VARCHAR(255) NOT NULL UNIQUE,
    headquarters VARCHAR(150) NULL,
    verification_status VARCHAR(50) NOT NULL DEFAULT 'UNVERIFIED',
    official_careers_url VARCHAR(500) NULL,
    known_email_domains TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS job_analyses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    raw_input_text MEDIUMTEXT NOT NULL,
    input_source_type VARCHAR(50) NOT NULL DEFAULT 'TEXT_PASTE',
    overall_risk_score INT NOT NULL,
    risk_classification VARCHAR(20) NOT NULL,
    confidence_score DECIMAL(5,4) NOT NULL,
    extracted_metadata TEXT NOT NULL,
    xai_token_highlights TEXT NULL,
    gemini_summary TEXT NULL,
    pdf_report_storage_path VARCHAR(500) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS analysis_indicators (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    analysis_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    explanation TEXT NOT NULL,
    severity VARCHAR(20) NOT NULL,
    score_impact INT NOT NULL
);

CREATE TABLE IF NOT EXISTS url_checks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    analysis_id BIGINT NULL,
    input_url TEXT NOT NULL,
    resolved_final_url TEXT NOT NULL,
    domain VARCHAR(255) NOT NULL,
    is_shortener BOOLEAN NOT NULL DEFAULT FALSE,
    has_valid_ssl BOOLEAN NOT NULL DEFAULT FALSE,
    is_typosquatting BOOLEAN NOT NULL DEFAULT FALSE,
    impersonated_target VARCHAR(255) NULL,
    risk_level VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS scam_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_user_id BIGINT NULL,
    company_name VARCHAR(200) NOT NULL,
    job_title VARCHAR(200) NOT NULL,
    recruiter_details VARCHAR(255) NULL,
    incident_description TEXT NOT NULL,
    demanded_amount DECIMAL(10,2) NULL DEFAULT 0.00,
    evidence_file_paths TEXT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED',
    admin_review_notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_user_id BIGINT NULL,
    action_type VARCHAR(100) NOT NULL,
    target_entity VARCHAR(100) NOT NULL,
    target_id BIGINT NOT NULL,
    details TEXT NULL,
    ip_address VARCHAR(45) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

