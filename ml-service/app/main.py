from contextlib import asynccontextmanager

from fastapi import FastAPI, File, HTTPException, Request, UploadFile

from app.schemas.predictions import CropRecommendationRequest, YieldPredictionRequest
from app.services.disease_service import DiseaseService
from app.services.model_service import ModelService


@asynccontextmanager
async def lifespan(app: FastAPI):
    app.state.model_service = ModelService()
    app.state.disease_service = DiseaseService()
    yield


app = FastAPI(title="Smart Farming ML Service", version="0.1.0", lifespan=lifespan)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "fastapi-ml-service"}


@app.get("/disease-detection/status")
def disease_detection_status(request: Request) -> dict[str, str | bool]:
    return request.app.state.disease_service.status()


@app.post("/disease-detection")
async def disease_detection(request: Request, image: UploadFile = File(...)) -> dict[str, str | float]:
    return await request.app.state.disease_service.detect(image)


@app.post("/crop-recommendation")
def crop_recommendation(payload: CropRecommendationRequest, request: Request) -> dict[str, str | int]:
    return request.app.state.model_service.recommend_crop(payload)


@app.post("/yield-prediction")
def yield_prediction(payload: YieldPredictionRequest, request: Request) -> dict[str, float]:
    try:
        return request.app.state.model_service.predict_yield(payload)
    except ValueError as error:
        raise HTTPException(status_code=422, detail=str(error)) from error
