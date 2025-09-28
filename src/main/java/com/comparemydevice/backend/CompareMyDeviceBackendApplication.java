package com.comparemydevice.backend;

import com.comparemydevice.backend.dto.*;
import com.comparemydevice.backend.entity.*;
import com.comparemydevice.backend.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
            // Dev-only reset (you already run with profile "mysql")
            flyway.clean();
            flyway.migrate();

            if (brandRepo.count() > 0) {
                log.info("Seed data already exists. Skipping JSON import.");
                return;
            }

            final ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Load JSONs
            List<BrandDTO> brandDTOs      = readJson(mapper, "json/brand.json",      new TypeReference<>() {});
            List<CategoryDTO> categoryDTOs= readJson(mapper, "json/category.json",   new TypeReference<>() {});
            List<TagDTO> tagDTOs          = readJson(mapper, "json/tag.json",        new TypeReference<>() {});
            List<SpecKeyDTO> specKeyDTOs  = readJson(mapper, "json/speckey.json",    new TypeReference<>() {});
            List<DeviceDTO> deviceDTOs    = readJson(mapper, "json/device.json",     new TypeReference<>() {});
            List<ImageDTO> imageDTOs      = readJson(mapper, "json/image.json",      new TypeReference<>() {});
            List<ReviewDTO> reviewDTOs    = readJson(mapper, "json/review.json",     new TypeReference<>() {});
            List<DeviceSpecDTO> dsDTOs    = readJson(mapper, "json/devicespec.json", new TypeReference<>() {});

            seedAll(
                    brandDTOs, categoryDTOs, tagDTOs, specKeyDTOs,
                    deviceDTOs, imageDTOs, reviewDTOs, dsDTOs,
                    brandRepo, categoryRepo, tagRepo, specKeyRepo,
                    deviceRepo, imageRepo, reviewRepo, deviceSpecRepo
            );
        };
    }

    private void seedAll(
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

        // 1) Save reference tables and build *stable-key* maps (by slug/name)
        List<Brand> savedBrands = brandRepo.saveAll(
                brandDTOs.stream().map(d ->
                        Brand.builder()
                                .name(d.getName())
                                .slug(d.getSlug())
                                .logoUrl(d.getLogoUrl())
                                .build()
                ).toList()
        );
        Map<String, Brand> brandBySlug = savedBrands.stream()
                .collect(Collectors.toMap(Brand::getSlug, Function.identity(), (a,b)->a, LinkedHashMap::new));

        List<Category> savedCategories = categoryRepo.saveAll(
                categoryDTOs.stream().map(d ->
                        Category.builder()
                                .name(d.getName())
                                .slug(d.getSlug())
                                .iconUrl(d.getIconUrl())
                                .build()
                ).toList()
        );
        Map<String, Category> categoryBySlug = savedCategories.stream()
                .collect(Collectors.toMap(Category::getSlug, Function.identity(), (a,b)->a, LinkedHashMap::new));

        List<Tag> savedTags = tagRepo.saveAll(
                tagDTOs.stream().map(d ->
                        Tag.builder()
                                .name(d.getName())
                                .slug(d.getSlug())
                                .build()
                ).toList()
        );
        Map<String, Tag> tagBySlug = savedTags.stream()
                .collect(Collectors.toMap(Tag::getSlug, Function.identity(), (a,b)->a, LinkedHashMap::new));

        List<SpecKey> savedSpecKeys = specKeyRepo.saveAll(
                specKeyDTOs.stream().map(d ->
                        SpecKey.builder()
                                .name(d.getName())
                                .specType(d.getSpecType())
                                .build()
                ).toList()
        );
        Map<String, SpecKey> specKeyByName = savedSpecKeys.stream()
                .collect(Collectors.toMap(SpecKey::getName, Function.identity(), (a,b)->a, LinkedHashMap::new));

        // Also build “oldId → newId” maps using DTOs’ stable keys
        Map<Long, Long> brandIdRemap = new LinkedHashMap<>();
        for (BrandDTO dto : brandDTOs) {
            Brand b = brandBySlug.get(dto.getSlug());
            if (b != null && dto.getId() != null) brandIdRemap.put(dto.getId(), b.getId());
        }
        Map<Long, Long> categoryIdRemap = new LinkedHashMap<>();
        for (CategoryDTO dto : categoryDTOs) {
            Category c = categoryBySlug.get(dto.getSlug());
            if (c != null && dto.getId() != null) categoryIdRemap.put(dto.getId(), c.getId());
        }
        Map<Long, Long> tagIdRemap = new LinkedHashMap<>();
        for (TagDTO dto : tagDTOs) {
            Tag t = tagBySlug.get(dto.getSlug());
            if (t != null && dto.getId() != null) tagIdRemap.put(dto.getId(), t.getId());
        }
        Map<Long, Long> specKeyIdRemap = new LinkedHashMap<>();
        for (SpecKeyDTO dto : specKeyDTOs) {
            SpecKey sk = specKeyByName.get(dto.getName()); // name is unique
            if (sk != null && dto.getId() != null) specKeyIdRemap.put(dto.getId(), sk.getId());
        }

        // 2) Devices (remap brand/category/tag ids)
        List<Device> savedDevices = deviceRepo.saveAll(
                deviceDTOs.stream().map(d -> {
                    Long newBrandId = brandIdRemap.get(d.getBrandId());
                    Long newCatId = categoryIdRemap.get(d.getCategoryId());
                    if (newBrandId == null) {
                        throw new IllegalStateException("Seed error: Brand not found for device '" +
                                d.getName() + "' with brandId=" + d.getBrandId());
                    }
                    if (newCatId == null) {
                        throw new IllegalStateException("Seed error: Category not found for device '" +
                                d.getName() + "' with categoryId=" + d.getCategoryId());
                    }
                    Brand brand = savedBrands.stream().filter(b -> Objects.equals(b.getId(), newBrandId)).findFirst().orElse(null);
                    Category cat = savedCategories.stream().filter(c -> Objects.equals(c.getId(), newCatId)).findFirst().orElse(null);

                    // remap tag ids
                    List<Long> newTagIds = Optional.ofNullable(d.getTagIds()).orElseGet(List::of).stream()
                            .map(tagIdRemap::get)
                            .filter(Objects::nonNull)
                            .toList();
                    LinkedHashSet<Tag> tags = newTagIds.stream()
                            .map(id -> savedTags.stream().filter(t -> Objects.equals(t.getId(), id)).findFirst().orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toCollection(LinkedHashSet::new));

                    return Device.builder()
                            .name(d.getName())
                            .processor(d.getProcessor())
                            .ram(d.getRam())
                            .storage(d.getStorage())
                            .priceAmount(d.getPriceAmount())
                            .priceCurrency(d.getPriceCurrency())
                            .releaseDate(d.getReleaseDate())
                            .slug(d.getSlug())
                            .isDeleted(Boolean.TRUE.equals(d.getIsDeleted()))
                            .brand(brand)
                            .category(cat)
                            .tags(tags)
                            .build();
                }).toList()
        );
        // map “old device id (from JSON) → persisted Device”
        Map<Long, Device> deviceByOldId = new LinkedHashMap<>();
        for (DeviceDTO d : deviceDTOs) {
            // match by slug (stable) to find the saved device
            Device saved = savedDevices.stream()
                    .filter(dev -> Objects.equals(dev.getSlug(), d.getSlug()))
                    .findFirst().orElse(null);
            if (saved != null && d.getId() != null) deviceByOldId.put(d.getId(), saved);
        }

        // 3) Images
        List<Image> images = imageDTOs.stream().map(d -> {
            Device device = deviceByOldId.get(d.getDeviceId());
            if (device == null) return null;
            return Image.builder()
                    .url(d.getUrl())
                    .altText(d.getAltText())
                    .isPrimary(Boolean.TRUE.equals(d.getIsPrimary()))
                    .sortOrder(d.getSortOrder())
                    .device(device)
                    .build();
        }).filter(Objects::nonNull).toList();
        imageRepo.saveAll(images);

        // 4) Reviews
        List<Review> reviews = reviewDTOs.stream().map(d -> {
            Device device = deviceByOldId.get(d.getDeviceId());
            if (device == null) return null;
            return Review.builder()
                    .reviewerName(d.getReviewerName())
                    .content(d.getContent())
                    .rating(d.getRating() == null ? null : new BigDecimal(d.getRating().toString()))
                    .sourceUrl(d.getSourceUrl())
                    .device(device)
                    .build();
        }).filter(Objects::nonNull).toList();
        reviewRepo.saveAll(reviews);

        // 5) Device Specs (remap both deviceId and specKeyId!)
        List<DeviceSpec> specs = deviceSpecDTOs.stream().map(d -> {
            Device device = deviceByOldId.get(d.getDeviceId());
            Long newSpecKeyId = specKeyIdRemap.get(d.getSpecKeyId());
            if (device == null || newSpecKeyId == null) return null;

            SpecKey specKey = savedSpecKeys.stream()
                    .filter(sk -> Objects.equals(sk.getId(), newSpecKeyId))
                    .findFirst().orElse(null);
            if (specKey == null) return null;

            return DeviceSpec.builder()
                    .device(device)
                    .specKey(specKey)
                    .valueText(d.getValueText())
                    .build();
        }).filter(Objects::nonNull).toList();
        deviceSpecRepo.saveAll(specs);

        log.info("✔ Seed completed: {} brands, {} categories, {} tags, {} spec keys, {} devices, {} images, {} reviews, {} device specs",
                savedBrands.size(), savedCategories.size(), savedTags.size(), savedSpecKeys.size(),
                savedDevices.size(), images.size(), reviews.size(), specs.size());
    }

    private static <T> List<T> readJson(ObjectMapper mapper, String path, TypeReference<List<T>> typeRef) {
        try (InputStream is = CompareMyDeviceBackendApplication.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) return List.of();
            return mapper.readValue(is, typeRef);
        } catch (Exception e) {
            log.error("❌ Failed to read JSON: {} -> {}", path, e.getMessage());
            return List.of();
        }
    }
}