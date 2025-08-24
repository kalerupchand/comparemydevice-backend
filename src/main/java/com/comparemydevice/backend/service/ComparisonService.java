// src/main/java/com/comparemydevice/backend/service/ComparisonService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;

import java.util.List;

public interface ComparisonService {
    List<DeviceDTO> compareByIds(List<Long> deviceIds);
}