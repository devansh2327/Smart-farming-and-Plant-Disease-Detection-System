from pathlib import Path

from fastapi import HTTPException, UploadFile

ROOT_DIRECTORY = Path(__file__).resolve().parents[3]
MODEL_PATH = ROOT_DIRECTORY / "Plant_Disease_Prediction" / "trained_plant_disease_model.keras"


class DiseaseService:
    """Reports model availability without producing predictions when the artifact is absent."""

    @property
    def is_available(self) -> bool:
        return MODEL_PATH.is_file()

    def status(self) -> dict[str, str | bool]:
        if self.is_available:
            return {"available": True, "message": "Plant disease model is available"}
        return {
            "available": False,
            "message": "Plant disease model file trained_plant_disease_model.keras was not found",
        }

    async def detect(self, image: UploadFile) -> dict[str, str | float]:
        filename = image.filename or ""
        has_image_extension = Path(filename).suffix.lower() in {".jpg", ".jpeg", ".png"}
        if not (image.content_type and image.content_type.startswith("image/")) and not has_image_extension:
            raise HTTPException(status_code=400, detail="An image file is required")
        if not self.is_available:
            raise HTTPException(status_code=503, detail=self.status()["message"])

        raise HTTPException(status_code=501, detail="Disease model loading is pending model artifact verification")
