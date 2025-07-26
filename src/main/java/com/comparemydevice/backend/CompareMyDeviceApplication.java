// File: com.comparemydevice.backend.CompareMyDeviceBackendApplication.java
package com.comparemydevice.backend;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
class CompareMyDeviceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompareMyDeviceBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(DeviceRepository deviceRepository) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Device>> typeReference = new TypeReference<>() {};

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("device-data.json");

            if (inputStream != null) {
                List<Device> devices = mapper.readValue(inputStream, typeReference);
                deviceRepository.saveAll(devices);
                System.out.println("✅ Loaded " + devices.size() + " devices into the database.");
            } else {
                System.err.println("❌ Could not find device-data.json in resources folder.");
            }
        };
    }
}