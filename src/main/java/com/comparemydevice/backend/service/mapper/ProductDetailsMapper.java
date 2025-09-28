package com.comparemydevice.backend.service.mapper;

import com.comparemydevice.backend.dto.ProductDetailsDTO;
import com.comparemydevice.backend.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductDetailsMapper {

    // Map your internal specType to “Flipkart-like” sections
    private static final Map<String, String> SECTION_BY_SPECTYPE = Map.ofEntries(
            Map.entry("general",      "General"),
            Map.entry("display",      "Display"),
            Map.entry("performance",  "Performance"),
            Map.entry("memory",       "Memory"),
            Map.entry("camera",       "Camera"),
            Map.entry("connectivity", "Connectivity"),
            Map.entry("battery",      "Battery"),
            Map.entry("dimensions",   "Dimensions"),
            Map.entry("software",     "Software"),
            Map.entry("warranty",     "Warranty"),
            Map.entry("feature",      "Highlights") // we’ll also parse to bullets
    );

    public ProductDetailsDTO toDetails(Device d) {
        // images: primary first, limit 5
        List<ProductDetailsDTO.ImageItem> images = Optional.ofNullable(d.getImages())
                .orElseGet(Set::of)
                .stream()
                .sorted(Comparator
                        .comparing(Image::getIsPrimary, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(i -> Optional.ofNullable(i.getSortOrder()).orElse(Integer.MAX_VALUE))
                        .thenComparing(Image::getId))
                .limit(5)
                .map(i -> ProductDetailsDTO.ImageItem.builder()
                        .url(i.getUrl())
                        .alt(i.getAltText())
                        .primary(Boolean.TRUE.equals(i.getIsPrimary()))
                        .build())
                .toList();

        // specs grouped by section
        Map<String, List<ProductDetailsDTO.SpecItem>> specsBySection = new LinkedHashMap<>();
        List<String> highlights = new ArrayList<>();
        String color = null;

        for (DeviceSpec ds : Optional.ofNullable(d.getDeviceSpecs()).orElseGet(Set::of)) {
            SpecKey key = ds.getSpecKey();
            if (key == null) continue;

            String specType = Optional.ofNullable(key.getSpecType()).orElse("general").toLowerCase(Locale.ROOT);
            String section  = SECTION_BY_SPECTYPE.getOrDefault(specType, "Other");
            String label    = Optional.ofNullable(key.getName()).orElse("—");
            String value    = ds.getValueText();

            // Build grouped specs
            specsBySection.computeIfAbsent(section, s -> new ArrayList<>())
                    .add(ProductDetailsDTO.SpecItem.builder()
                            .label(label)
                            .value(value)
                            .build());

            // Extract highlights as bullets if the key is “Highlights”
            if ("feature".equals(specType) || "Highlights".equalsIgnoreCase(label)) {
                highlights.addAll(splitBullets(value));
            }

            // Extract color if available
            if ("Color".equalsIgnoreCase(label) && color == null) {
                color = value;
            }
        }

        // reviews
        List<ProductDetailsDTO.ReviewItem> reviews = Optional.ofNullable(d.getReviews())
                .orElseGet(Set::of)
                .stream()
                .sorted(Comparator.comparing(Review::getId).reversed())
                .map(r -> ProductDetailsDTO.ReviewItem.builder()
                        .reviewer(r.getReviewerName())
                        .content(r.getContent())
                        .rating(r.getRating() == null ? null : r.getRating().doubleValue())
                        .sourceUrl(r.getSourceUrl())
                        .build())
                .toList();

        return ProductDetailsDTO.builder()
                .id(d.getId())
                .name(d.getName())
                .brand(d.getBrand() != null ? d.getBrand().getName() : null)
                .category(d.getCategory() != null ? d.getCategory().getName() : null)
                .color(color)
                .priceAmount(d.getPriceAmount())
                .priceCurrency(d.getPriceCurrency())
                .slug(d.getSlug())
                .images(images)
                .highlights(distinctPreserveOrder(highlights))
                .specs(specsBySection)
                .reviews(reviews)
                .build();
    }

    private static List<String> splitBullets(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        // supports • bullets, newlines, or commas
        return Arrays.stream(raw.split("\\r?\\n|•|\\u2022|,"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private static <T> List<T> distinctPreserveOrder(List<T> list) {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }
}