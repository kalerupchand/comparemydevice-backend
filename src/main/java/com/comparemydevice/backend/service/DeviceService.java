package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.dto.DeviceSpecDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

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

    // unified filters for list endpoint
    List<DeviceDTO> listFiltered(String q, Long brandId, Long categoryId, Long tagId);

    // paged search
    Page<DeviceDTO> search(String q, Long brandId, Long categoryId, Long tagId, Pageable pageable);

    // compare
    List<DeviceDTO> findByIds(List<Long> ids);

    // search suggestions
    List<String> getSearchSuggestions(String query);

    // 🔹 New optional features for spec-level comparisons
    List<DeviceSpecDTO> getSpecsForDevice(Long deviceId);

    Map<String, Map<Long, String>> compareSpecs(List<Long> deviceIds);
}