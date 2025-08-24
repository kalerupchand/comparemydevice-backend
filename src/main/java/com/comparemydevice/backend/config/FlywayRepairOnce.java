// src/main/java/com/comparemydevice/backend/config/FlywayRepairOnce.java
package com.comparemydevice.backend.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class FlywayRepairOnce {
    @Bean
    ApplicationRunner flywayRepairRunner(Flyway flyway) {
        return args -> {
            // This will remove entries for missing migrations and fix checksums.
            flyway.repair();
        };
    }
}