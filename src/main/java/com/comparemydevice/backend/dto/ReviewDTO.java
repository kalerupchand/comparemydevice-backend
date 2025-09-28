package com.comparemydevice.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDTO {
    private Long id;
    private String reviewerName;
    private String content;
    private BigDecimal rating;   // DECIMAL(2,1) -> BigDecimal
    private String sourceUrl;

    private Long deviceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}