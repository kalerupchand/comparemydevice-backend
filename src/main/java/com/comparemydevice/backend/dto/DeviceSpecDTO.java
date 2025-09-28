package com.comparemydevice.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceSpecDTO {
    private Long id;

    private Long deviceId;     // maps to device_spec.device_id
    private Long specKeyId;    // maps to device_spec.spec_key_id

    // denormalized read-only helpers
    private String specKeyName; // spec_key.name
    private String specType;    // spec_key.spec_type

    private String valueText;   // device_spec.value_text

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}