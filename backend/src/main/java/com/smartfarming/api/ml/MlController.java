package com.smartfarming.api.ml;

import com.smartfarming.api.ml.dto.CropRecommendationRequest;
import com.smartfarming.api.ml.dto.YieldPredictionRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ml")
public class MlController {
    private final MlServiceClient mlServiceClient;
    private final ObjectMapper objectMapper;

    public MlController(MlServiceClient mlServiceClient, ObjectMapper objectMapper) {
        this.mlServiceClient = mlServiceClient;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/crop-recommendation")
    public Map<String, Object> cropRecommendation(@Valid @RequestBody CropRecommendationRequest request) {
        return invoke(() -> mlServiceClient.cropRecommendation(request));
    }

    @PostMapping("/yield-prediction")
    public Map<String, Object> yieldPrediction(@Valid @RequestBody YieldPredictionRequest request) {
        return invoke(() -> mlServiceClient.yieldPrediction(request));
    }

    private Map<String, Object> invoke(ProviderCall call) {
        try {
            return call.execute();
        } catch (HttpStatusCodeException error) {
            throw new ResponseStatusException(error.getStatusCode(), mlErrorMessage(error), error);
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "ML service is unavailable or rejected the request", error);
        }
    }

    private String mlErrorMessage(HttpStatusCodeException error) {
        try {
            JsonNode response = objectMapper.readTree(error.getResponseBodyAsString());
            if (response.hasNonNull("detail")) {
                return response.get("detail").asText();
            }
        } catch (Exception ignored) {
            // Fall through to the generic provider response message.
        }
        return "ML service rejected the request";
    }

    @FunctionalInterface
    private interface ProviderCall {
        Map<String, Object> execute();
    }
}
