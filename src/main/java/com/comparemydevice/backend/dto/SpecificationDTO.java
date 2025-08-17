package com.comparemydevice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// --- SpecificationDTO ---
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationDTO {
    private Long id;
    private String key;
    private String value;
    private String specType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}