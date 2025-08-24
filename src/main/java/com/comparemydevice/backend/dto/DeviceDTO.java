// src/main/java/com/comparemydevice/backend/dto/DeviceDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class DeviceDTO {
    private Long id;
    private String name;
    private String processor;
    private String ram;
    private String storage;

    // if your entity still uses priceInINR BigDecimal, keep it:
    private BigDecimal priceAmount;
    private String priceCurrency; // default "INR"

    private LocalDate releaseDate;
    private String slug;
    private Boolean isDeleted;

    private Long brandId;
    private Long categoryId;

    // tags both as ids and hydrated objects for convenience in read ops
    private List<Long> tagIds;
    private List<TagDTO> tags;

    private List<ImageDTO> images;
    private List<ReviewDTO> reviews;
    private List<DeviceSpecDTO> specifications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}