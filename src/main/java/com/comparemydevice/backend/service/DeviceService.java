// src/main/java/com/comparemydevice/backend/service/DeviceService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceService {
    DeviceDTO create(DeviceDTO dto);
    DeviceDTO get(Long id);
    List<DeviceDTO> getAll();

    DeviceDTO update(Long id, DeviceDTO dto);
    void delete(Long id);

    // existing helpers
    List<DeviceDTO> findByBrand(Long brandId);
    List<DeviceDTO> findByCategory(Long categoryId);
    List<DeviceDTO> findByTag(Long tagId);

    // new: unified filters for list endpoint
    List<DeviceDTO> listFiltered(String q, Long brandId, Long categoryId, Long tagId);

    // new: paged search (for /api/devices/search)
    Page<DeviceDTO> search(String q, Long brandId, Long categoryId, Long tagId, Pageable pageable);

    // compare
    List<DeviceDTO> findByIds(List<Long> ids);
}