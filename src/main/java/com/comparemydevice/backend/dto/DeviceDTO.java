package com.comparemydevice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// --- DeviceDTO ---
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO {
    private Long id;
    private String name;
    private String processor;
    private String ram;
    private String storage;
    private BigDecimal priceInINR; // ✅ Changed from Integer to BigDecimal to match NUMERIC(10,2)
    private LocalDate releaseDate; // ✅ Use LocalDate for DATE column
    private String slug;
    private List<String> tags; // ✅ TEXT[] → List<String>
    private Boolean isDeleted;
    private BrandDTO brand;
    private CategoryDTO category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SpecificationDTO> specifications;
    private List<ImageDTO> images;
    private List<ReviewDTO> reviews;
}
