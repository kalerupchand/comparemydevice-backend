package com.comparemydevice.backend.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SpecKeyDTO {
    private Long id;
    private String name;      // aligns with spec_key.name
    private String specType;  // aligns with spec_key.spec_type
}