package com.smartfarming.api.chatbot;

import java.util.Locale;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {
    @PostMapping
    public Map<String, String> reply(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").toLowerCase(Locale.ROOT);
        String reply = message.contains("weather") || message.contains("मौसम")
                ? "Use the Weather page to check local temperature and rainfall forecast before irrigation."
                : message.contains("water") || message.contains("irrig") || message.contains("पानी") || message.contains("सिंचाई")
                ? "Irrigate early morning or evening. Avoid waterlogging and adjust watering based on soil moisture and rain forecast."
                : message.contains("disease") || message.contains("diseas") || message.contains("रोग")
                ? "Remove badly affected leaves, keep tools clean, and consult a local agriculture officer before applying any pesticide."
                : message.contains("fertil") || message.contains("उर्वरक") || message.contains("खाद")
                ? "Use a soil test before choosing fertilizer. Apply recommended doses in split applications and avoid overuse."
                : "Ask me about weather, irrigation, crop disease, fertilizer, or farming schemes. / मौसम, सिंचाई, रोग या खाद के बारे में पूछें।";
        return Map.of("reply", reply);
    }
}
