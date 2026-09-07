package com.smartfarming.api.weather;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    private final String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherService(@Value("${app.weather-api-key:}") String apiKey) { this.apiKey = apiKey; }

    public Map<String, Object> getWeather(String city) {
        if (apiKey.isBlank()) throw new IllegalStateException("OpenWeather API key is not configured on the server");
        Map<String, Object> current = get("https://api.openweathermap.org/data/2.5/weather", city);
        Map<String, Object> forecast = get("https://api.openweathermap.org/data/2.5/forecast", city);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("city", current.get("name"));
        result.put("current", current);
        result.put("forecast", dailyForecast(forecast));
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> dailyForecast(Map<String, Object> forecast) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : (List<Map<String, Object>>) forecast.getOrDefault("list", List.of())) {
            if (String.valueOf(item.get("dt_txt")).contains("12:00:00")) result.add(item);
            if (result.size() == 5) break;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> get(String baseUrl, String city) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("q", city)
                .queryParam("units", "metric").queryParam("appid", apiKey).toUriString();
        return restTemplate.getForObject(url, Map.class);
    }
}
