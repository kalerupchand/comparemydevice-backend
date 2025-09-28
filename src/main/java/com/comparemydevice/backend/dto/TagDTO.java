package com.comparemydevice.backend.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TagDTO {
    private Long id;
    private String name;
    private String slug;
}