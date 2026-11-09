from fastapi import FastAPI, Header, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, Dict, Any, List
import os

from app.pipeline.extractor import JobEntityExtractor
from app.pipeline.classifier import HybridClassifier
from app.pipeline.xai_explainer import XaiExplainer

app = FastAPI(
    title="JobShield AI - ML Inference Service",
    description="Explainable NLP and fraud risk extraction service for JobShield AI",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

INTERNAL_SECRET = os.getenv("INTERNAL_SECRET", "JobShieldInternalMLKey_2026")

class PredictRequest(BaseModel):
    text: str
    sourceUrl: Optional[str] = None

class HealthResponse(BaseModel):
    status: str
    version: str
    model_type: str

@app.get("/internal/v1/health", response_model=HealthResponse)
def health_check():
    return {
        "status": "HEALTHY",
        "version": "1.0.0",
        "model_type": "TF-IDF + Calibrated Heuristic Ensemble"
    }

@app.post("/internal/v1/predict")
def predict_fraud(
    payload: PredictRequest,
    x_internal_secret: Optional[str] = Header(None)
):
    if x_internal_secret != INTERNAL_SECRET and os.getenv("ENV") == "production":
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid internal authorization secret"
        )

    text = payload.text
    if not text or len(text.strip()) < 10:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Input text must be at least 10 characters long"
        )

    # 1. Extract named entities
    entities = JobEntityExtractor.extract_all(text)

    # 2. Compute ML & heuristic probability
    fraud_prob, details = HybridClassifier.predict_risk(text)

    # 3. Compute XAI token attribution highlights
    highlights = XaiExplainer.attribute_tokens(text)

    return {
        "fraud_probability": fraud_prob,
        "is_fraud": fraud_prob >= 0.50,
        "confidence": 0.92,
        "extracted_entities": entities,
        "token_highlights": highlights,
        "model_details": details,
        "model_version": "v1.0-calibrated"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8001, reload=True)

