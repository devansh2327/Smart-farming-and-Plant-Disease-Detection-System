package com.smartfarming.api.farmer;

import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("postgres")
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
