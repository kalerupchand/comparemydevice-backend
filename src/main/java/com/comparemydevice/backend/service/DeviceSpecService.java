// src/main/java/com/comparemydevice/backend/service/SpecificationService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceSpecDTO;

import java.util.List;

public interface DeviceSpecService {
    DeviceSpecDTO create(DeviceSpecDTO dto);
    DeviceSpecDTO get(Long id);
    DeviceSpecDTO update(Long id, DeviceSpecDTO dto);
    void delete(Long id);
    List<DeviceSpecDTO> getAll();
}