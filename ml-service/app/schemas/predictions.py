from pydantic import BaseModel, Field


class CropRecommendationRequest(BaseModel):
    nitrogen: float = Field(ge=0, le=140)
    phosphorus: float = Field(ge=5, le=145)
    potassium: float = Field(ge=5, le=205)
    temperature: float = Field(ge=8.825674745, le=43.67549305)
    humidity: float = Field(ge=14.25803981, le=99.98187601)
    ph: float = Field(ge=3.504752314, le=9.93509073)
    rainfall: float = Field(ge=20.21126747, le=298.5601175)


class YieldPredictionRequest(BaseModel):
    year: int = Field(ge=1990, le=2013)
    average_rainfall_mm_per_year: float = Field(ge=51, le=3240)
    pesticides_tonnes: float = Field(ge=0.04, le=367778)
    average_temperature: float = Field(ge=1.3, le=30.65)
    area: str = Field(min_length=1)
    item: str = Field(min_length=1)
