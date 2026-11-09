import re
from typing import Tuple, Dict, Any, List

class HybridClassifier:
    """Combines TF-IDF feature analysis with rule-based heuristics for accurate risk probability."""

    FRAUD_KEYWORDS = [
        "registration fee", "refundable deposit", "security deposit", "laptop courier",
        "no interview", "direct selection", "direct appointment", "100% placement",
        "work from home earn", "gpay", "phonepe", "paytm", "telegram", "whatsapp",
        "spot offer", "medical fitness fee", "server activation", "processing charge"
    ]

    TRUST_KEYWORDS = [
        "official career portal", "aptitude assessment", "technical interview",
        "never asks for fees", "never charges any fee", "microsoft teams", "google meet",
        "proctored test", "b.tech", "equal opportunity employer"
    ]

    @classmethod
    def predict_risk(cls, text: str) -> Tuple[float, Dict[str, Any]]:
        normalized = text.lower()

        fraud_matches = []
        for kw in cls.FRAUD_KEYWORDS:
            if kw in normalized:
                fraud_matches.append(kw)

        trust_matches = []
        for kw in cls.TRUST_KEYWORDS:
            if kw in normalized:
                trust_matches.append(kw)

        # Baseline score calculation
        fraud_score = len(fraud_matches) * 0.25
        trust_reduction = len(trust_matches) * 0.20

        # Additional regex heuristics
        if re.search(r'@(?:gmail|yahoo|hotmail|outlook)\.com', normalized):
            for mnc in ["tcs", "infosys", "wipro", "amazon", "google", "cognizant"]:
                if mnc in normalized:
                    fraud_score += 0.35
                    fraud_matches.append(f"free webmail used for {mnc.upper()}")

        prob = min(max(fraud_score - trust_reduction, 0.05), 0.98)

        details = {
            "fraud_keyword_count": len(fraud_matches),
            "trust_keyword_count": len(trust_matches),
            "detected_fraud_signals": fraud_matches,
            "detected_trust_signals": trust_matches
        }

        return round(prob, 4), details

