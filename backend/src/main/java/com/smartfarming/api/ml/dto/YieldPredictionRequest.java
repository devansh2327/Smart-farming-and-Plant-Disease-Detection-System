package com.smartfarming.api.ml.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record YieldPredictionRequest(
        @NotNull @DecimalMin("1990") @DecimalMax("2013") Integer year,
        @NotNull @DecimalMin("51") @DecimalMax("3240") Double averageRainfallMmPerYear,
        @NotNull @DecimalMin("0.04") @DecimalMax("367778") Double pesticidesTonnes,
        @NotNull @DecimalMin("1.3") @DecimalMax("30.65") Double averageTemperature,
        @NotBlank String area,
        @NotBlank String item) {
}
