package com.smartfarming.api.schemes;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/government-schemes")
public class GovernmentSchemeController {
    @GetMapping
    public List<Map<String, String>> schemes() {
        return List.of(
                Map.of("name", "PM-KISAN", "benefits", "Income support for eligible farmer families.", "eligibility", "Eligible landholding farmer families, subject to scheme rules.", "application", "Apply through pmkisan.gov.in or a local Common Service Centre."),
                Map.of("name", "Pradhan Mantri Fasal Bima Yojana", "benefits", "Crop-insurance support against notified crop losses.", "eligibility", "Farmers growing notified crops in notified areas.", "application", "Contact an insurer, bank, agriculture office, or pmfby.gov.in."),
                Map.of("name", "Soil Health Card Scheme", "benefits", "Soil test results and nutrient-management guidance.", "eligibility", "Farmers seeking soil testing through local agriculture services.", "application", "Visit the local agriculture department or soil-testing laboratory."));
    }
}
