package com.smartfarming.api.ml.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CropRecommendationRequest(
        @NotNull @DecimalMin("0") @DecimalMax("140") Double nitrogen,
        @NotNull @DecimalMin("5") @DecimalMax("145") Double phosphorus,
        @NotNull @DecimalMin("5") @DecimalMax("205") Double potassium,
        @NotNull @DecimalMin("8.825674745") @DecimalMax("43.67549305") Double temperature,
        @NotNull @DecimalMin("14.25803981") @DecimalMax("99.98187601") Double humidity,
        @NotNull @DecimalMin("3.504752314") @DecimalMax("9.93509073") Double ph,
        @NotNull @DecimalMin("20.21126747") @DecimalMax("298.5601175") Double rainfall) {
}
