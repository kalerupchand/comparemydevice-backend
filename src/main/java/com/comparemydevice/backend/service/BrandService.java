// src/main/java/com/comparemydevice/backend/service/BrandService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.BrandDTO;

import java.util.List;

public interface BrandService {
    BrandDTO create(BrandDTO dto);
    BrandDTO get(Long id);
    BrandDTO update(Long id, BrandDTO dto);
    void delete(Long id);
    List<BrandDTO> getAll();
}