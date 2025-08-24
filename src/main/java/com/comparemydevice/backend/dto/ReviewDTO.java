// src/main/java/com/comparemydevice/backend/dto/ReviewDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long deviceId;
    private String reviewerName;
    private String content;
    private BigDecimal rating;    // 0..5 (validated in service)
    private String sourceUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}