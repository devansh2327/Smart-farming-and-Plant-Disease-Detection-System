package com.smartfarming.api.farmer.dto;

public record AuthResponse(String token, FarmerProfileResponse farmer) {
}
