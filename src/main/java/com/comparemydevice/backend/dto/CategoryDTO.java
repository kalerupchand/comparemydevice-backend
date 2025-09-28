package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private String iconUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}