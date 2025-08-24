// src/main/java/com/comparemydevice/backend/service/ImageService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.ImageDTO;

import java.util.List;

public interface ImageService {
    ImageDTO create(ImageDTO dto);
    ImageDTO get(Long id);
    ImageDTO update(Long id, ImageDTO dto);
    void delete(Long id);
    List<ImageDTO> getAll();
}