import re
from typing import List, Dict, Any

class XaiExplainer:
    """Computes Explainable AI (XAI) token attribution weights for front-end visual text highlighting."""

    CRITICAL_PATTERNS = [
        (r'registration\s*fee(?:\s*(?:of|rs\.?|₹)?\s*[\d,]+)?', "Upfront payment demand (+40)"),
        (r'refundable\s*(?:security\s*)?deposit', "False promise of refundable fees (+35)"),
        (r'no\s*interview(?:\s*required)?', "Bypasses standard screening (+30)"),
        (r'direct\s*(?:selection|appointment)', "Instant offer without vetting (+30)"),
        (r'(?:gpay|phonepe|paytm|upi\s*scanner)', "Personal payment rail (+35)"),
        (r'telegram\s*(?:group|channel|@[\w_]+)', "Unregulated communication (+25)"),
        (r'[a-zA-Z0-9_.+-]+@(?:gmail|yahoo|hotmail)\.com', "Unverified free webmail address (+35)")
    ]

    TRUST_PATTERNS = [
        (r'never\s*(?:asks\s*for|charges)\s*(?:any\s*)?fees?', "Explicit zero-fee compliance policy (-25)"),
        (r'official\s*career\s*portal', "Verified corporate infrastructure (-20)"),
        (r'https?://(?:[a-zA-Z0-9-]+\.)*(?:tcs\.com|infosys\.com|wipro\.com|amazon\.jobs|google\.com)', "Legitimate corporate domain (-25)"),
        (r'(?:microsoft\s*teams|google\s*meet)\b', "Enterprise video conferencing (-15)")
    ]

    @classmethod
    def attribute_tokens(cls, text: str) -> List[Dict[str, Any]]:
        highlights = []

        # Find critical matches
        for pattern, reason in cls.CRITICAL_PATTERNS:
            for match in re.finditer(pattern, text, re.IGNORECASE):
                highlights.append({
                    "token": match.group(0),
                    "score": 0.85,
                    "type": "CRITICAL",
                    "reason": reason
                })

        # Find trust matches
        for pattern, reason in cls.TRUST_PATTERNS:
            for match in re.finditer(pattern, text, re.IGNORECASE):
                highlights.append({
                    "token": match.group(0),
                    "score": -0.75,
                    "type": "TRUST",
                    "reason": reason
                })

        return highlights

