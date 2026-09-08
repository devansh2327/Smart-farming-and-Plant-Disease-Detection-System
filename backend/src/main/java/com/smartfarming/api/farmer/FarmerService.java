package com.smartfarming.api.farmer;

import com.smartfarming.api.config.JwtService;
import com.smartfarming.api.farmer.dto.AuthResponse;
import com.smartfarming.api.farmer.dto.FarmerProfileResponse;
import com.smartfarming.api.farmer.dto.LoginRequest;
import com.smartfarming.api.farmer.dto.RegisterRequest;
import com.smartfarming.api.farmer.dto.UpdateProfileRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Profile("postgres")
public class FarmerService {
    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public FarmerService(FarmerRepository farmerRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizedEmail(request.email());
        if (farmerRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
        Farmer farmer = new Farmer(request.fullName().trim(), email, passwordEncoder.encode(request.password()));
        farmer.updateProfile(farmer.getFullName(), email, blankToNull(request.phoneNumber()), null, null, null, null);
        Farmer saved = farmerRepository.save(farmer);
        return new AuthResponse(jwtService.createToken(saved.getEmail()), FarmerProfileResponse.from(saved));
    }

    public AuthResponse login(LoginRequest request) {
        Farmer farmer = findByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), farmer.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return new AuthResponse(jwtService.createToken(farmer.getEmail()), FarmerProfileResponse.from(farmer));
    }

    public FarmerProfileResponse profile(String email) {
        return FarmerProfileResponse.from(findByEmail(email));
    }

    @Transactional
    public AuthResponse updateProfile(String currentEmail, UpdateProfileRequest request) {
        Farmer farmer = findByEmail(currentEmail);
        String email = normalizedEmail(request.email());
        if (!farmer.getEmail().equals(email) && farmerRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
        farmer.updateProfile(request.fullName().trim(), email, blankToNull(request.phoneNumber()),
                blankToNull(request.farmLocation()), request.farmSize(), blankToNull(request.soilType()),
                blankToNull(request.irrigationType()));
        Farmer saved = farmerRepository.save(farmer);
        return new AuthResponse(jwtService.createToken(saved.getEmail()), FarmerProfileResponse.from(saved));
    }

    private Farmer findByEmail(String email) {
        return farmerRepository.findByEmailIgnoreCase(normalizedEmail(email))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    }

    private String normalizedEmail(String email) { return email.trim().toLowerCase(); }
    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
