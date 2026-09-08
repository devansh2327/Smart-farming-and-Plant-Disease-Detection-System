package com.smartfarming.api.farmer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Email @Size(max = 160) String email,
        @Size(max = 30) String phoneNumber,
        @Size(max = 160) String farmLocation,
        @DecimalMin(value = "0.01", message = "Farm size must be greater than zero") BigDecimal farmSize,
        @Size(max = 80) String soilType,
        @Size(max = 80) String irrigationType) {
}
