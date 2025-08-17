package com.comparemydevice.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// --- ImageDTO ---
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    private Long id;
    private String url;
    private String altText;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}