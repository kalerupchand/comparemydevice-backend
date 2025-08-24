// src/main/java/com/comparemydevice/backend/dto/DeviceSpecDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class DeviceSpecDTO {
    private Long id;
    private Long deviceId;
    private Long specKeyId;

    // derived/read-only helpers
    private String specKeyName;   // maps from SpecKey.key (human label)
    private String specType;

    private String valueText;     // aligns with entity DeviceSpec.valueText

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}