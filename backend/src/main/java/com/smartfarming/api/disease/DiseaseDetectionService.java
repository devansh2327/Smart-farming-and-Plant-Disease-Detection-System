package com.smartfarming.api.disease;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DiseaseDetectionService {
    private final String mlServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public DiseaseDetectionService(@Value("${app.ml-service-url}") String mlServiceUrl) {
        this.mlServiceUrl = mlServiceUrl;
    }

    public Map<String, Object> status() {
        return restTemplate.getForObject(mlServiceUrl + "/disease-detection/status", Map.class);
    }

    public Map<String, Object> detect(MultipartFile image) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image", image.getResource()).filename(image.getOriginalFilename());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return restTemplate.postForObject(mlServiceUrl + "/disease-detection",
                new HttpEntity<>(bodyBuilder.build(), headers), Map.class);
    }
}
