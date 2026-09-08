package com.smartfarming.api.farmer;

import com.smartfarming.api.farmer.dto.AuthResponse;
import com.smartfarming.api.farmer.dto.LoginRequest;
import com.smartfarming.api.farmer.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("postgres")
@RequestMapping("/api/auth")
public class AuthController {
    private final FarmerService farmerService;

    public AuthController(FarmerService farmerService) { this.farmerService = farmerService; }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) { return farmerService.register(request); }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) { return farmerService.login(request); }
}
