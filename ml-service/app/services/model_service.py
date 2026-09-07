import pickle
from pathlib import Path

import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler

from app.schemas.predictions import CropRecommendationRequest, YieldPredictionRequest

ROOT_DIRECTORY = Path(__file__).resolve().parents[3]
CROP_DIRECTORY = ROOT_DIRECTORY / "Crop-Recommendation" / "Crop-Recommendation-System-Using-Machine-Learning-main"
YIELD_DIRECTORY = ROOT_DIRECTORY / "Crop-Yields-Prediction" / "Predicting-Crop-Yields-Crop-Yield-Prediction-Enhancing-Agriculture-with-Machine-Learning-Hindi-master"
CROP_FEATURES = ["N", "P", "K", "temperature", "humidity", "ph", "rainfall"]
CROP_LABELS = {
    1: "Rice", 2: "Maize", 3: "Jute", 4: "Cotton", 5: "Coconut", 6: "Papaya",
    7: "Orange", 8: "Apple", 9: "Muskmelon", 10: "Watermelon", 11: "Grapes",
    12: "Mango", 13: "Banana", 14: "Pomegranate", 15: "Lentil", 16: "Blackgram",
    17: "Mungbean", 18: "Mothbeans", 19: "Pigeonpeas", 20: "Kidneybeans",
    21: "Chickpea", 22: "Coffee",
}


class ModelService:
    def __init__(self) -> None:
        self.crop_model = self._load_pickle(CROP_DIRECTORY / "model.pkl")
        self.crop_scaler = self._regenerate_crop_scaler()
        self.yield_model = self._load_pickle(YIELD_DIRECTORY / "models" / "dtr.pkl")
        self.yield_preprocessor = self._load_pickle(YIELD_DIRECTORY / "models" / "preprocessor.pkl")
        self.yield_areas = set(self.yield_preprocessor.named_transformers_["OHE"].categories_[0])
        self.yield_items = set(self.yield_preprocessor.named_transformers_["OHE"].categories_[1])

    @staticmethod
    def _load_pickle(path: Path):
        with path.open("rb") as model_file:
            return pickle.load(model_file)

    @staticmethod
    def _regenerate_crop_scaler() -> MinMaxScaler:
        """Recreate the notebook's fitted scaler without changing its corrupted artifact."""
        dataset = pd.read_csv(CROP_DIRECTORY / "Crop_recommendation.csv")
        training_features, _ = train_test_split(dataset[CROP_FEATURES], test_size=0.2, random_state=42)
        return MinMaxScaler().fit(training_features)

    def recommend_crop(self, payload: CropRecommendationRequest) -> dict[str, str | int]:
        features = pd.DataFrame([[
            payload.nitrogen, payload.phosphorus, payload.potassium, payload.temperature,
            payload.humidity, payload.ph, payload.rainfall,
        ]], columns=CROP_FEATURES)
        predicted_class = int(self.crop_model.predict(self.crop_scaler.transform(features))[0])
        return {"crop": CROP_LABELS[predicted_class], "modelClass": predicted_class}

    def predict_yield(self, payload: YieldPredictionRequest) -> dict[str, float]:
        if payload.area not in self.yield_areas:
            raise ValueError("area is not supported by the yield model")
        if payload.item not in self.yield_items:
            raise ValueError("item is not supported by the yield model")

        features = pd.DataFrame([[
            payload.year, payload.average_rainfall_mm_per_year, payload.pesticides_tonnes,
            payload.average_temperature, payload.area, payload.item,
        ]], columns=[
            "Year", "average_rain_fall_mm_per_year", "pesticides_tonnes", "avg_temp", "Area", "Item",
        ])
        transformed_features = self.yield_preprocessor.transform(features)
        prediction = float(self.yield_model.predict(transformed_features)[0])
        return {"predictedYield": prediction}
