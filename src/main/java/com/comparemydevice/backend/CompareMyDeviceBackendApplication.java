package com.comparemydevice.backend;

import com.comparemydevice.backend.entity.*;
import com.comparemydevice.backend.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class CompareMyDeviceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompareMyDeviceBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(
            BrandRepository brandRepository,
            CategoryRepository categoryRepository,
            DeviceRepository deviceRepository,
            ImageRepository imageRepository,
            ReviewRepository reviewRepository,
            SpecificationRepository specificationRepository
    ) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            loadDataForEntity("json/brand.json", new TypeReference<List<Brand>>() {}, brandRepository, mapper);
            loadDataForEntity("json/category.json", new TypeReference<List<Category>>() {}, categoryRepository, mapper);
            loadDataForEntity("json/device.json", new TypeReference<List<Device>>() {}, deviceRepository, mapper);
            loadDataForEntity("json/image.json", new TypeReference<List<Image>>() {}, imageRepository, mapper);
            loadDataForEntity("json/review.json", new TypeReference<List<Review>>() {}, reviewRepository, mapper);
            loadDataForEntity("json/specification.json", new TypeReference<List<Specification>>() {}, specificationRepository, mapper);
        };
    }

    private <T> void loadDataForEntity(String fileName, TypeReference<List<T>> typeRef,
                                       org.springframework.data.jpa.repository.JpaRepository<T, Long> repo,
                                       ObjectMapper mapper) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                List<T> entities = mapper.readValue(inputStream, typeRef);
                repo.saveAll(entities);
                System.out.println("✅ Loaded " + entities.size() + " records from " + fileName);
            } else {
                System.err.println("❌ Could not find " + fileName + " in resources folder.");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to load data from " + fileName);
            e.printStackTrace();
        }
    }
}