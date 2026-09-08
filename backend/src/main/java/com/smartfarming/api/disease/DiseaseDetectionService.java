package com.smartfarming.api.disease;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        try {
            ByteArrayResource imageResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", imageResource);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            return restTemplate.postForObject(mlServiceUrl + "/disease-detection",
                    new HttpEntity<>(body, headers), Map.class);
        } catch (java.io.IOException error) {
            throw new IllegalArgumentException("Unable to read the uploaded image", error);
        }
    }
}
