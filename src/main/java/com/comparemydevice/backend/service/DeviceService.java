// src/main/java/com/comparemydevice/backend/service/DeviceService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;

import java.util.List;

public interface DeviceService {
    DeviceDTO create(DeviceDTO dto);
    DeviceDTO get(Long id);
    DeviceDTO update(Long id, DeviceDTO dto);
    void delete(Long id);
    List<DeviceDTO> getAll();

    List<DeviceDTO> findByBrand(Long brandId);
    List<DeviceDTO> findByCategory(Long categoryId);
    List<DeviceDTO> findByTag(Long tagId);
}