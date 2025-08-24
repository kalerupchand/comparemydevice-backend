// src/main/java/com/comparemydevice/backend/dto/CategoryDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String iconUrl;
    private String slug;              // SEO-friendly
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}