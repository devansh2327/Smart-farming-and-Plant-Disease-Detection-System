package com.smartfarming.api.market;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market-prices")
public class MarketPriceController {
    @GetMapping
    public List<Map<String, Object>> prices() {
        return List.of(
                Map.of("crop", "Rice", "market", "Delhi Mandi", "price", 2450, "unit", "₹/quintal"),
                Map.of("crop", "Wheat", "market", "Ludhiana Mandi", "price", 2350, "unit", "₹/quintal"),
                Map.of("crop", "Maize", "market", "Indore Mandi", "price", 2200, "unit", "₹/quintal"),
                Map.of("crop", "Potato", "market", "Agra Mandi", "price", 1800, "unit", "₹/quintal"),
                Map.of("crop", "Tomato", "market", "Nashik Mandi", "price", 3200, "unit", "₹/quintal"));
    }
}
