package com.smartfarming.api.disease;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ml/disease-detection")
public class DiseaseDetectionController {
    private final DiseaseDetectionService diseaseDetectionService;

    public DiseaseDetectionController(DiseaseDetectionService diseaseDetectionService) {
        this.diseaseDetectionService = diseaseDetectionService;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        try {
            return diseaseDetectionService.status();
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "ML service is unavailable", error);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> detect(@RequestParam("image") MultipartFile image) {
        try {
            Map<String, Object> modelStatus = diseaseDetectionService.status();
            if (Boolean.FALSE.equals(modelStatus.get("available"))) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        String.valueOf(modelStatus.get("message")));
            }
            return diseaseDetectionService.detect(image);
        } catch (HttpStatusCodeException error) {
            throw new ResponseStatusException(error.getStatusCode(), error.getResponseBodyAsString(), error);
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Disease model is unavailable", error);
        }
    }
}
