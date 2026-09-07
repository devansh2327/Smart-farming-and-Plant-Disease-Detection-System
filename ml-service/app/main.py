from fastapi import FastAPI

app = FastAPI(title="Smart Farming ML Service", version="0.1.0")


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "fastapi-ml-service"}
