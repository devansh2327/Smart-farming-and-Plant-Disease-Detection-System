package com.smartfarming.api.weather;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) { this.weatherService = weatherService; }

    @GetMapping
    public Map<String, Object> current(@RequestParam String city) {
        try { return weatherService.getWeather(city); }
        catch (IllegalStateException error) { throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, error.getMessage(), error); }
        catch (Exception error) { throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Weather information is currently unavailable", error); }
    }
}
