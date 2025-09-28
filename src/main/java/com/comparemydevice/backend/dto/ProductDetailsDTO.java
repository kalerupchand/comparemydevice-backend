package com.comparemydevice.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductDetailsDTO {
    private Long id;
    private String name;
    private String brand;       // e.g., "Apple"
    private String category;    // e.g., "Smartphone"
    private String color;       // optional if present in specs
    private BigDecimal priceAmount;
    private String priceCurrency;
    private String slug;

    // gallery (limit 5; primary first)
    private List<ImageItem> images;

    // bullet points (from "Highlights" spec or composed)
    private List<String> highlights;

    // specs grouped like Flipkart sections: "General", "Display", etc.
    // key = section title, value = list of label/value pairs
    private Map<String, List<SpecItem>> specs;

    private List<ReviewItem> reviews;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ImageItem {
        private String url;
        private String alt;
        private Boolean primary;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SpecItem {
        private String label;
        private String value;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ReviewItem {
        private String reviewer;
        private String content;
        private Double rating;   // keep simple for UI
        private String sourceUrl;
    }
}