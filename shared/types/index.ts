export type Role = 'ROLE_JOB_SEEKER' | 'ROLE_ADMIN' | 'ROLE_EMPLOYER';

export type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH';

export type IndicatorSeverity = 'POSITIVE' | 'INFO' | 'WARNING' | 'CRITICAL';

export type InputSourceType = 'TEXT_PASTE' | 'URL' | 'PDF_DOCUMENT' | 'SCREENSHOT_OCR';

export type ReportStatus = 'SUBMITTED' | 'UNDER_REVIEW' | 'VERIFIED_SUSPICIOUS' | 'UNABLE_TO_VERIFY' | 'CLOSED';

export type CompanyVerificationStatus = 'UNVERIFIED' | 'PENDING_REVIEW' | 'VERIFIED_LEGITIMATE' | 'FLAGGED_SUSPICIOUS';

export interface User {
  id: number;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: Role;
  isActive: boolean;
  createdAt: string;
}

export interface AuthResponse {
  userId: number;
  email: string;
  fullName: string;
  role: Role;
  token: string;
}

export interface ExtractedJobMetadata {
  companyName: string;
  jobTitle: string;
  salary: string;
  experienceRequired: string;
  location: string;
  employmentType: string;
  requiredSkills: string[];
  recruiterName: string;
  recruiterEmail: string;
  recruiterPhone: string;
  applicationUrl: string;
  demandedFee: string;
  interviewPlatform: string;
}

export interface RiskIndicator {
  id: number;
  category: 'FINANCIAL_EXTORTION' | 'DOMAIN_MISMATCH' | 'UNREALISTIC_OFFER' | 'SUSPICIOUS_URGENCY' | 'VAGUE_DESCRIPTION' | 'COMMUNICATION_RISK' | 'POSITIVE_TRUST';
  title: string;
  explanation: string;
  severity: IndicatorSeverity;
  scoreImpact: number;
}

export interface XaiTokenHighlight {
  token: string;
  score: number;
  type: 'CRITICAL' | 'WARNING' | 'TRUST' | 'NEUTRAL';
  reason?: string;
}

export interface UrlCheckResult {
  inputUrl: string;
  resolvedFinalUrl: string;
  domain: string;
  isShortener: boolean;
  hasValidSsl: boolean;
  isTyposquatting: boolean;
  impersonatedBrand?: string;
  riskLevel: 'SAFE' | 'SUSPICIOUS' | 'MALICIOUS';
  hops?: Array<{ url: string; status: number }>;
}

export interface JobAnalysisResult {
  analysisId: number;
  rawInputText: string;
  inputSourceType: InputSourceType;
  overallRiskScore: number;
  riskClassification: RiskLevel;
  confidenceScore: number;
  extractedMetadata: ExtractedJobMetadata;
  indicators: RiskIndicator[];
  xaiTokenHighlights: XaiTokenHighlight[];
  urlAnalysis?: UrlCheckResult;
  geminiSummary: string;
  createdAt: string;
}

export interface ScamReport {
  id: number;
  companyName: string;
  jobTitle: string;
  recruiterContact: string;
  incidentDescription: string;
  demandedAmount: number;
  evidenceUrls: string[];
  status: ReportStatus;
  adminReviewNotes?: string;
  reporterEmail?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Company {
  id: number;
  name: string;
  normalizedName: string;
  officialDomain: string;
  headquarters: string;
  verificationStatus: CompanyVerificationStatus;
  officialCareersUrl: string;
  knownEmailDomains: string[];
}

export interface AuditLog {
  id: number;
  adminEmail: string;
  actionType: string;
  targetEntity: string;
  targetId: number;
  details: string;
  ipAddress: string;
  createdAt: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  error: {
    code: string;
    message: string;
    details?: any;
  } | null;
  timestamp: string;
}

