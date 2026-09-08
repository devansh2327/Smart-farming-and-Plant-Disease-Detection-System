package com.smartfarming.api.farmer.dto;

import com.smartfarming.api.farmer.Farmer;
import java.math.BigDecimal;

public record FarmerProfileResponse(Long id, String fullName, String email, String phoneNumber,
                                    String farmLocation, BigDecimal farmSize, String soilType,
                                    String irrigationType, String role) {
    public static FarmerProfileResponse from(Farmer farmer) {
        return new FarmerProfileResponse(farmer.getId(), farmer.getFullName(), farmer.getEmail(),
                farmer.getPhoneNumber(), farmer.getFarmLocation(), farmer.getFarmSize(),
                farmer.getSoilType(), farmer.getIrrigationType(), farmer.getRole().name());
    }
}
