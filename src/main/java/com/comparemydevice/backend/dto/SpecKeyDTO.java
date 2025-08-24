// src/main/java/com/comparemydevice/backend/dto/SpecKeyDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class SpecKeyDTO {
    private Long id;
    private String name;    // ✅ keep this
    private String specType;
}