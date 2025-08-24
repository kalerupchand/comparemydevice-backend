// src/main/java/com/comparemydevice/backend/service/TagService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO create(TagDTO dto);
    TagDTO get(Long id);
    TagDTO update(Long id, TagDTO dto);
    void delete(Long id);
    List<TagDTO> getAll();
}