package com.smartfarming.api.farmer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "farmers")
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(length = 30)
    private String phoneNumber;

    @Column(length = 160)
    private String farmLocation;

    private BigDecimal farmSize;

    @Column(length = 80)
    private String soilType;

    @Column(length = 80)
    private String irrigationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FarmerRole role;

    protected Farmer() {
    }

    public Farmer(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = FarmerRole.FARMER;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFarmLocation() { return farmLocation; }
    public BigDecimal getFarmSize() { return farmSize; }
    public String getSoilType() { return soilType; }
    public String getIrrigationType() { return irrigationType; }
    public FarmerRole getRole() { return role; }

    public void updateProfile(String fullName, String email, String phoneNumber, String farmLocation,
                              BigDecimal farmSize, String soilType, String irrigationType) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.farmLocation = farmLocation;
        this.farmSize = farmSize;
        this.soilType = soilType;
        this.irrigationType = irrigationType;
    }
}
