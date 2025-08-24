// src/main/java/com/comparemydevice/backend/dto/BrandDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BrandDTO {
    private Long id;
    private String name;
    private String logoUrl;
    private String slug;              // ensure brand has slug for future-friendly URLs
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}