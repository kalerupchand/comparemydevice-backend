package com.comparemydevice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// --- BrandDTO ---
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {
    private Long id;
    private String name;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

