// src/main/java/com/comparemydevice/backend/dto/TagDTO.java
package com.comparemydevice.backend.dto;

import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private String slug;     // required (we fixed schema + entity)
}