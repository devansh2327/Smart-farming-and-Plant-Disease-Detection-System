package com.smartfarming.api.ml;

import com.smartfarming.api.ml.dto.CropRecommendationRequest;
import com.smartfarming.api.ml.dto.YieldPredictionRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ml")
public class MlController {
    private final MlServiceClient mlServiceClient;

    public MlController(MlServiceClient mlServiceClient) {
        this.mlServiceClient = mlServiceClient;
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
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "ML service is unavailable or rejected the request", error);
        }
    }

    @FunctionalInterface
    private interface ProviderCall {
        Map<String, Object> execute();
    }
}
