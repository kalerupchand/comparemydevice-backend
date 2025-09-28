package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BrandDTO {
    private Long id;
    private String name;
    private String slug;
    private String logoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}