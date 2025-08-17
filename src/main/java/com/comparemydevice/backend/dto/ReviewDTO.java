package com.comparemydevice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// --- ReviewDTO ---
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private String reviewerName;
    private String content;
    private BigDecimal rating; // ✅ Use BigDecimal to support 2,1 precision
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // ✅ Add updatedAt field per schema
}