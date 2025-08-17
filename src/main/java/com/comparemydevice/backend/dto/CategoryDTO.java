package com.comparemydevice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// --- CategoryDTO ---
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String iconUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
