package com.comparemydevice.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.repository.DeviceRepository;

@SpringBootApplication
class CompareMyDeviceBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompareMyDeviceBackendApplication.class, args);
    }

    // Optional seed data
    @Bean
    public CommandLineRunner demo(DeviceRepository repository) {
        return (args) -> {
            repository.save(new Device("Samsung Galaxy S24", "Snapdragon 8 Gen 3", "8GB", "128GB"));
            repository.save(new Device("iPhone 15", "A17 Bionic", "6GB", "128GB"));
            repository.save(new Device("OnePlus 12", "Snapdragon 8 Gen 3", "12GB", "256GB"));
        };
    }
}