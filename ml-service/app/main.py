from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException, Request

from app.schemas.predictions import CropRecommendationRequest, YieldPredictionRequest
from app.services.model_service import ModelService


@asynccontextmanager
async def lifespan(app: FastAPI):
    app.state.model_service = ModelService()
    yield


app = FastAPI(title="Smart Farming ML Service", version="0.1.0", lifespan=lifespan)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "fastapi-ml-service"}


@app.post("/crop-recommendation")
def crop_recommendation(payload: CropRecommendationRequest, request: Request) -> dict[str, str | int]:
    return request.app.state.model_service.recommend_crop(payload)


@app.post("/yield-prediction")
def yield_prediction(payload: YieldPredictionRequest, request: Request) -> dict[str, float]:
    try:
        return request.app.state.model_service.predict_yield(payload)
    except ValueError as error:
        raise HTTPException(status_code=422, detail=str(error)) from error
