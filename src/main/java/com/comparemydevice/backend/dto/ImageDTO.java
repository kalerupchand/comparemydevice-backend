// src/main/java/com/comparemydevice/backend/dto/ImageDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class ImageDTO {
    private Long id;
    private Long deviceId;
    private String url;
    private String altText;
    private Boolean isPrimary;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}