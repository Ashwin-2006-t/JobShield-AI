import re
from typing import Dict, Any, List

class JobEntityExtractor:
    """Extracts job posting metadata and key entities using regex & contextual rules."""

    @staticmethod
    def extract_all(text: str) -> Dict[str, Any]:
        normalized = text.strip()

        # 1. Email extraction
        email_matches = re.findall(r'[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+', normalized)
        recruiter_email = email_matches[0] if email_matches else "Not found"

        # 2. Phone / WhatsApp extraction
        phone_matches = re.findall(r'(?:\+?91[\s-]?)?[6-9]\d{9}', normalized)
        recruiter_phone = phone_matches[0] if phone_matches else "Not found"

        # 3. URL extraction
        url_matches = re.findall(r'https?://(?:www\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b[-a-zA-Z0-9()@:%_+.~#?&/=]*', normalized)
        application_url = url_matches[0] if url_matches else "Not found"

        # 4. Salary / Compensation extraction
        salary_match = re.search(r'(?:₹|Rs\.?|INR)\s*[\d,]+(?:\s*-\s*[\d,]+)?(?:\s*(?:LPA|per\s*month|per\s*annum|monthly|yearly))?', normalized, re.IGNORECASE)
        salary = salary_match.group(0) if salary_match else "Not explicitly specified"

        # 5. Upfront Fee / Deposit extraction
        fee_match = re.search(r'(?:fee|deposit|charge|amount|payment|cost)\s*(?:of)?\s*(?:₹|Rs\.?|INR)?\s*[\d,]+', normalized, re.IGNORECASE)
        demanded_fee = fee_match.group(0) if fee_match else "None detected"

        # 6. Interview platform
        interview_platform = "Standard / Corporate"
        if re.search(r'\btelegram\b', normalized, re.IGNORECASE):
            interview_platform = "Telegram (High Risk vector)"
        elif re.search(r'\bwhatsapp\b', normalized, re.IGNORECASE):
            interview_platform = "WhatsApp Direct Chat"
        elif re.search(r'\bgoogle\s*meet\b', normalized, re.IGNORECASE):
            interview_platform = "Google Meet"
        elif re.search(r'\bmicrosoft\s*teams\b', normalized, re.IGNORECASE):
            interview_platform = "Microsoft Teams"

        # 7. Experience level
        exp_match = re.search(r'(\d+(?:\s*-\s*\d+)?\s*(?:years?|yrs?)|fresher|no\s*experience)', normalized, re.IGNORECASE)
        experience = exp_match.group(0) if exp_match else "Fresher / Unspecified"

        # 8. Likely company & role heuristic
        company_name = "Unspecified Organization"
        known_companies = ["Tata Consultancy Services", "TCS", "Infosys", "Wipro", "Amazon", "Google", "Cognizant", "Accenture", "HCLTech", "Microsoft"]
        for c in known_companies:
            if re.search(rf'\b{re.escape(c)}\b', normalized, re.IGNORECASE):
                company_name = c
                break

        job_title = "General Position"
        roles = ["Data Entry Operator", "Software Engineer", "Systems Engineer", "Back Office Assistant", "Frontend Developer", "Ground Staff", "Quality Assurance", "Customer Support"]
        for r in roles:
            if re.search(rf'\b{re.escape(r)}\b', normalized, re.IGNORECASE):
                job_title = r
                break

        return {
            "companyName": company_name,
            "jobTitle": job_title,
            "salary": salary,
            "experienceRequired": experience,
            "location": "Remote / Specified in text",
            "employmentType": "Full-time / Remote",
            "requiredSkills": ["Communication", "Problem Solving"],
            "recruiterName": "Recruiter / Talent Acquisition",
            "recruiterEmail": recruiter_email,
            "recruiterPhone": recruiter_phone,
            "applicationUrl": application_url,
            "demandedFee": demanded_fee,
            "interviewPlatform": interview_platform
        }

