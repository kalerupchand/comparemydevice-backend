package com.comparemydevice.backend;

import com.comparemydevice.backend.dto.*;
import com.comparemydevice.backend.entity.*;
import com.comparemydevice.backend.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class CompareMyDeviceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompareMyDeviceBackendApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner seedDevData(
            Flyway flyway,
            BrandRepository brandRepo,
            CategoryRepository categoryRepo,
            TagRepository tagRepo,
            SpecKeyRepository specKeyRepo,
            DeviceRepository deviceRepo,
            ImageRepository imageRepo,
            ReviewRepository reviewRepo,
            DeviceSpecRepository deviceSpecRepo
    ) {
        return args -> {
            flyway.migrate();

            if (brandRepo.count() > 0) {
                log.info("Seed data already exists. Skipping JSON import.");
                return;
            }

            final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

            // Read JSON files
            List<BrandDTO> brands = readJson(mapper, "json/brand.json", new TypeReference<>() {});
            List<CategoryDTO> categories = readJson(mapper, "json/category.json", new TypeReference<>() {});
            List<TagDTO> tags = readJson(mapper, "json/tag.json", new TypeReference<>() {});
            List<SpecKeyDTO> specKeys = readJson(mapper, "json/speckey.json", new TypeReference<>() {});
            List<DeviceDTO> devices = readJson(mapper, "json/device.json", new TypeReference<>() {});
            List<ImageDTO> images = readJson(mapper, "json/image.json", new TypeReference<>() {});
            List<ReviewDTO> reviews = readJson(mapper, "json/review.json", new TypeReference<>() {});
            List<DeviceSpecDTO> deviceSpecs = readJson(mapper, "json/devicespec.json", new TypeReference<>() {});

            // Save data in proper FK order
            seedAll(brands, categories, tags, specKeys, devices, images, reviews, deviceSpecs,
                    brandRepo, categoryRepo, tagRepo, specKeyRepo,
                    deviceRepo, imageRepo, reviewRepo, deviceSpecRepo);
        };
    }

    @Transactional
    protected void seedAll(
            List<BrandDTO> brandDTOs,
            List<CategoryDTO> categoryDTOs,
            List<TagDTO> tagDTOs,
            List<SpecKeyDTO> specKeyDTOs,
            List<DeviceDTO> deviceDTOs,
            List<ImageDTO> imageDTOs,
            List<ReviewDTO> reviewDTOs,
            List<DeviceSpecDTO> deviceSpecDTOs,
            BrandRepository brandRepo,
            CategoryRepository categoryRepo,
            TagRepository tagRepo,
            SpecKeyRepository specKeyRepo,
            DeviceRepository deviceRepo,
            ImageRepository imageRepo,
            ReviewRepository reviewRepo,
            DeviceSpecRepository deviceSpecRepo
    ) {
        // Brands
        List<Brand> brands = brandDTOs.stream()
                .map(d -> Brand.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .slug(d.getSlug())
                        .logoUrl(d.getLogoUrl())
                        .build())
                .collect(Collectors.toList());
        brandRepo.saveAll(brands);
        Map<Long, Brand> brandById = brands.stream()
                .filter(b -> b.getId() != null)
                .collect(Collectors.toMap(Brand::getId, Function.identity()));

        // Categories
        List<Category> categories = categoryDTOs.stream()
                .map(d -> Category.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .slug(d.getSlug())
                        .iconUrl(d.getIconUrl())
                        .build())
                .collect(Collectors.toList());
        categoryRepo.saveAll(categories);
        Map<Long, Category> categoryById = categories.stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        // Tags
        List<Tag> tags = tagDTOs.stream()
                .map(d -> Tag.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .slug(d.getSlug())
                        .build())
                .collect(Collectors.toList());
        tagRepo.saveAll(tags);
        Map<Long, Tag> tagById = tags.stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(Tag::getId, Function.identity()));

        // Spec Keys
        List<SpecKey> specKeys = specKeyDTOs.stream()
                .map(d -> SpecKey.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .specType(d.getSpecType())
                        .build())
                .collect(Collectors.toList());
        specKeyRepo.saveAll(specKeys);
        Map<Long, SpecKey> specKeyById = specKeys.stream()
                .filter(sk -> sk.getId() != null)
                .collect(Collectors.toMap(SpecKey::getId, Function.identity()));

        // Devices
        List<Device> devices = deviceDTOs.stream()
                .map(d -> Device.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .processor(d.getProcessor())
                        .ram(d.getRam())
                        .storage(d.getStorage())
                        .priceAmount(d.getPriceAmount())
                        .priceCurrency(d.getPriceCurrency())
                        .releaseDate(d.getReleaseDate())
                        .slug(d.getSlug())
                        .isDeleted(Boolean.TRUE.equals(d.getIsDeleted()))
                        .brand(brandById.get(d.getBrandId()))
                        .category(categoryById.get(d.getCategoryId()))
                        .tags(Optional.ofNullable(d.getTagIds())
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(tagById::get)
                                .collect(Collectors.toCollection(LinkedHashSet::new)))
                        .build())
                .collect(Collectors.toList());
        deviceRepo.saveAll(devices);
        Map<Long, Device> deviceById = devices.stream()
                .filter(dev -> dev.getId() != null)
                .collect(Collectors.toMap(Device::getId, Function.identity()));

        // Images
        List<Image> images = imageDTOs.stream()
                .map(d -> Image.builder()
                        .id(d.getId())
                        .url(d.getUrl())
                        .altText(d.getAltText())
                        .isPrimary(Boolean.TRUE.equals(d.getIsPrimary()))
                        .device(deviceById.get(d.getDeviceId()))
                        .build())
                .collect(Collectors.toList());
        imageRepo.saveAll(images);

        // Reviews
        List<Review> reviews = reviewDTOs.stream()
                .map(d -> Review.builder()
                        .id(d.getId())
                        .reviewerName(d.getReviewerName())
                        .content(d.getContent())
                        .rating(new BigDecimal(d.getRating().toString()))
                        .sourceUrl(d.getSourceUrl())
                        .device(deviceById.get(d.getDeviceId()))
                        .build())
                .collect(Collectors.toList());
        reviewRepo.saveAll(reviews);

        // Device Specs
        List<DeviceSpec> specs = deviceSpecDTOs.stream()
                .map(d -> DeviceSpec.builder()
                        .id(d.getId())
                        .device(deviceById.get(d.getDeviceId()))
                        .specKey(specKeyById.get(d.getSpecKeyId()))
                        .valueText(d.getValueText())
                        .build())
                .collect(Collectors.toList());
        deviceSpecRepo.saveAll(specs);

        log.info("✔ Seed completed: {} brands, {} categories, {} tags, {} spec keys, {} devices, {} images, {} reviews, {} device specs",
                brands.size(), categories.size(), tags.size(), specKeys.size(),
                devices.size(), images.size(), reviews.size(), specs.size());
    }

    private static <T> List<T> readJson(ObjectMapper mapper, String path, TypeReference<List<T>> typeRef) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return mapper.readValue(is, typeRef);
        } catch (Exception e) {
            log.error("❌ Failed to read JSON: {} -> {}", path, e.getMessage());
            return Collections.emptyList();
        }
    }
}