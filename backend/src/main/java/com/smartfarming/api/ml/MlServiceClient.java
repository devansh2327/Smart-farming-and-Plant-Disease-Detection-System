package com.smartfarming.api.ml;

import com.smartfarming.api.ml.dto.CropRecommendationRequest;
import com.smartfarming.api.ml.dto.YieldPredictionRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MlServiceClient {
    private final String mlServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public MlServiceClient(@Value("${app.ml-service-url}") String mlServiceUrl) {
        this.mlServiceUrl = mlServiceUrl;
    }

    public Map<String, Object> cropRecommendation(CropRecommendationRequest request) {
        Map<String, Object> payload = Map.of(
                "nitrogen", request.nitrogen(), "phosphorus", request.phosphorus(),
                "potassium", request.potassium(), "temperature", request.temperature(),
                "humidity", request.humidity(), "ph", request.ph(), "rainfall", request.rainfall());
        return post("/crop-recommendation", payload);
    }

    public Map<String, Object> yieldPrediction(YieldPredictionRequest request) {
        Map<String, Object> payload = Map.of(
                "year", request.year(),
                "average_rainfall_mm_per_year", request.averageRainfallMmPerYear(),
                "pesticides_tonnes", request.pesticidesTonnes(),
                "average_temperature", request.averageTemperature(),
                "area", request.area(),
                "item", request.item());
        return post("/yield-prediction", payload);
    }

    private Map<String, Object> post(String path, Map<String, Object> payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(mlServiceUrl + path, new HttpEntity<>(payload, headers), Map.class);
    }
}
