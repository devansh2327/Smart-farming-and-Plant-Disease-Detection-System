from pathlib import Path

import numpy as np
import tensorflow as tf
from fastapi import HTTPException, UploadFile

ROOT_DIRECTORY = Path(__file__).resolve().parents[3]
MODEL_PATH = ROOT_DIRECTORY / "Plant_Disease_Prediction" / "trained_plant_disease_model.keras"
IMAGE_SIZE = (128, 128)

# This is the original project's class_name order; model output indexes rely on it.
DISEASE_LABELS = [
    "Apple___Apple_scab", "Apple___Black_rot", "Apple___Cedar_apple_rust", "Apple___healthy",
    "Blueberry___healthy", "Cherry_(including_sour)___Powdery_mildew", "Cherry_(including_sour)___healthy",
    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot", "Corn_(maize)___Common_rust_",
    "Corn_(maize)___Northern_Leaf_Blight", "Corn_(maize)___healthy", "Grape___Black_rot",
    "Grape___Esca_(Black_Measles)", "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)", "Grape___healthy",
    "Orange___Haunglongbing_(Citrus_greening)", "Peach___Bacterial_spot", "Peach___healthy",
    "Pepper,_bell___Bacterial_spot", "Pepper,_bell___healthy", "Potato___Early_blight",
    "Potato___Late_blight", "Potato___healthy", "Raspberry___healthy", "Soybean___healthy",
    "Squash___Powdery_mildew", "Strawberry___Leaf_scorch", "Strawberry___healthy",
    "Tomato___Bacterial_spot", "Tomato___Early_blight", "Tomato___Late_blight", "Tomato___Leaf_Mold",
    "Tomato___Septoria_leaf_spot", "Tomato___Spider_mites Two-spotted_spider_mite", "Tomato___Target_Spot",
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus", "Tomato___Tomato_mosaic_virus", "Tomato___healthy",
]


def load_disease_model():
    """Load the unchanged legacy model under Keras 3.

    The artifact's first Conv2D layer contains the obsolete
    ``batch_input_shape`` config key. Keras 3 rejects that key even though
    the model also has its own InputLayer. Removing it only in memory keeps
    the supplied model file and its weights untouched.
    """
    original_from_config = tf.keras.layers.Conv2D.from_config

    def compatible_from_config(cls, config):
        config = dict(config)
        config.pop("batch_input_shape", None)
        return original_from_config(config)

    tf.keras.layers.Conv2D.from_config = classmethod(compatible_from_config)
    try:
        return tf.keras.models.load_model(MODEL_PATH, compile=False)
    finally:
        tf.keras.layers.Conv2D.from_config = original_from_config


class DiseaseService:
    def __init__(self) -> None:
        self.model = None
        self.load_error: str | None = None
        if not MODEL_PATH.is_file():
            self.load_error = "Plant disease model file trained_plant_disease_model.keras was not found"
            return

        try:
            self.model = load_disease_model()
            if tuple(self.model.input_shape[1:]) != (128, 128, 3):
                raise ValueError(f"expected input shape (128, 128, 3), got {self.model.input_shape}")
            if self.model.output_shape[-1] != len(DISEASE_LABELS):
                raise ValueError(f"expected {len(DISEASE_LABELS)} output classes, got {self.model.output_shape}")
        except Exception as error:
            self.model = None
            self.load_error = f"Plant disease model could not be loaded: {error}"

    @property
    def is_available(self) -> bool:
        return self.model is not None

    def status(self) -> dict[str, str | bool]:
        if self.is_available:
            return {"available": True, "message": "Plant disease model is available"}
        return {"available": False, "message": self.load_error or "Plant disease model is unavailable"}

    async def detect(self, image: UploadFile) -> dict[str, str | float]:
        filename = image.filename or ""
        has_image_extension = Path(filename).suffix.lower() in {".jpg", ".jpeg", ".png"}
        if not (image.content_type and image.content_type.startswith("image/")) and not has_image_extension:
            raise HTTPException(status_code=400, detail="An image file is required")
        if not self.is_available:
            raise HTTPException(status_code=503, detail=self.status()["message"])

        try:
            decoded = tf.io.decode_image(await image.read(), channels=3, expand_animations=False)
            resized = tf.image.resize(decoded, IMAGE_SIZE)
            batch = tf.expand_dims(resized, axis=0)
            probabilities = self.model.predict(batch, verbose=0)[0]
        except Exception as error:
            raise HTTPException(status_code=400, detail=f"Unable to process the image: {error}") from error

        class_index = int(np.argmax(probabilities))
        disease = DISEASE_LABELS[class_index]
        return {
            "disease": disease,
            "confidence": float(probabilities[class_index]),
            "guidance": self._guidance(disease),
        }

    @staticmethod
    def _guidance(disease: str) -> str:
        if disease.endswith("___healthy"):
            return "The leaf appears healthy. Continue regular monitoring and balanced crop care."
        return "Remove heavily affected leaves, keep foliage dry, and consult a local agriculture officer before applying a crop-appropriate treatment."
