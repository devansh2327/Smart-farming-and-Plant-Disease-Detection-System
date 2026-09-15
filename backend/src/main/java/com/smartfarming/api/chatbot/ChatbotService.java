package com.smartfarming.api.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChatbotService {
    static final String SYSTEM_INSTRUCTION = """
            You are an intelligent agricultural assistant integrated into a Smart Farming and Plant Disease Detection System.

            Provide practical, clear and easy-to-understand information related to:
            - Crop selection
            - Crop cultivation
            - Soil
            - Irrigation
            - Fertilizers
            - Crop diseases
            - Plant health
            - Pest management
            - Weather-related farming decisions
            - Agricultural practices
            - Crop yield improvement
            - General farming guidance

            Give concise and useful answers suitable for farmers and agriculture students.

            Do not fabricate precise agricultural facts when you are uncertain.
            For serious plant disease, pesticide, fertilizer, or safety-related decisions, recommend consulting a qualified agricultural expert or local agricultural authority.

            If the user asks something unrelated to agriculture, politely explain that you are primarily an agricultural assistant.
            """;

    private static final int MAX_HISTORY_MESSAGES = 16;
    private static final String UNAVAILABLE = "The farm assistant is unavailable right now. Please try again.";

    private final String apiKey;
    private final String model;
    private final RestTemplate restTemplate;

    public ChatbotService(
            @Value("${app.gemini-api-key:}") String apiKey,
            @Value("${app.gemini-model:gemini-2.0-flash}") String model) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(30_000);
        this.restTemplate = new RestTemplate(factory);
    }

    public String reply(String message, List<Map<String, String>> history) {
        if (apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "The farm assistant is not configured on the server.");
        }

        Map<String, Object> payload = Map.of(
                "system_instruction", Map.of("parts", List.of(Map.of("text", SYSTEM_INSTRUCTION))),
                "contents", contents(message, history),
                "generationConfig", Map.of("temperature", 0.4, "maxOutputTokens", 2048));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(payload, headers), Map.class);
            return extractText(response.getBody());
        } catch (HttpStatusCodeException error) {
            throw mapGeminiHttpError(error);
        } catch (ResourceAccessException error) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT,
                    "The farm assistant took too long to respond. Please try again.");
        } catch (ResponseStatusException error) {
            throw error;
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
    }

    private List<Map<String, Object>> contents(String message, List<Map<String, String>> history) {
        List<Map<String, Object>> contents = new ArrayList<>();
        if (history != null && !history.isEmpty()) {
            int start = Math.max(0, history.size() - MAX_HISTORY_MESSAGES);
            for (Map<String, String> item : history.subList(start, history.size())) {
                if (item == null) {
                    continue;
                }
                String text = String.valueOf(item.getOrDefault("text", "")).trim();
                if (text.isEmpty()) {
                    continue;
                }
                String from = String.valueOf(item.getOrDefault("from", ""));
                String role = "You".equalsIgnoreCase(from) || "user".equalsIgnoreCase(from) ? "user" : "model";
                contents.add(turn(role, text));
            }
        }
        contents.add(turn("user", message));
        return contents;
    }

    private static Map<String, Object> turn(String role, String text) {
        return Map.of("role", role, "parts", List.of(Map.of("text", text)));
    }

    private String extractText(Map<String, Object> body) {
        if (body == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
        Object promptFeedback = body.get("promptFeedback");
        if (promptFeedback instanceof Map<?, ?> feedback && feedback.get("blockReason") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "I could not generate a response for that question. Please rephrase it.");
        }
        Object candidates = body.get("candidates");
        if (!(candidates instanceof List<?> candidateList) || candidateList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
        Object first = candidateList.get(0);
        if (!(first instanceof Map<?, ?> candidate)) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
        Object content = candidate.get("content");
        if (!(content instanceof Map<?, ?> contentMap)) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
        Object parts = contentMap.get("parts");
        if (!(parts instanceof List<?> partList) || partList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
        StringBuilder text = new StringBuilder();
        for (Object part : partList) {
            if (part instanceof Map<?, ?> partMap && partMap.get("text") != null) {
                text.append(partMap.get("text"));
            }
        }
        String reply = text.toString().trim();
        if (reply.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
        }
        return reply;
    }

    private ResponseStatusException mapGeminiHttpError(HttpStatusCodeException error) {
        HttpStatus status = HttpStatus.resolve(error.getStatusCode().value());
        if (status == HttpStatus.TOO_MANY_REQUESTS) {
            return new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "The farm assistant is busy right now. Please wait a moment and try again.");
        }
        if (status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN) {
            return new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "The farm assistant could not be authenticated. Please try again later.");
        }
        if (status == HttpStatus.BAD_REQUEST) {
            String body = error.getResponseBodyAsString();
            if (body != null && body.toLowerCase().contains("api key")) {
                return new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "The farm assistant could not be authenticated. Please try again later.");
            }
            return new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "I could not generate a response for that question. Please rephrase it.");
        }
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, UNAVAILABLE);
    }
}
