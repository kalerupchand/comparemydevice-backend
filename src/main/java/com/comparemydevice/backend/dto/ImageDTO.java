package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ImageDTO {
    private Long id;
    private String url;
    private String altText;
    private Boolean isPrimary;
    private Integer sortOrder;

    private Long deviceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}