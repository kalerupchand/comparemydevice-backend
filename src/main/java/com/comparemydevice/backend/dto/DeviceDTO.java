package com.comparemydevice.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceDTO {
    private Long id;

    private String name;
    private String processor;
    private String ram;
    private String storage;

    private BigDecimal priceAmount;
    private String priceCurrency;

    private LocalDate releaseDate;
    private String slug;
    private Boolean isDeleted;

    private Long brandId;
    private Long categoryId;

    /** Lists for API convenience; entity may use Set internally */
    private List<Long> tagIds;
    private List<TagDTO> tags;

    private List<ImageDTO> images;
    private List<ReviewDTO> reviews;
    private List<DeviceSpecDTO> deviceSpecs;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}