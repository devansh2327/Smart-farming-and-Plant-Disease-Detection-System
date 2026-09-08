package com.smartfarming.api.farmer;

import com.smartfarming.api.farmer.dto.FarmerProfileResponse;
import com.smartfarming.api.farmer.dto.AuthResponse;
import com.smartfarming.api.farmer.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("postgres")
@RequestMapping("/api/farmers/me")
public class FarmerProfileController {
    private final FarmerService farmerService;

    public FarmerProfileController(FarmerService farmerService) { this.farmerService = farmerService; }

    @GetMapping
    public FarmerProfileResponse profile(Authentication authentication) {
        return farmerService.profile(authentication.getName());
    }

    @PutMapping
    public AuthResponse update(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return farmerService.updateProfile(authentication.getName(), request);
    }
}
