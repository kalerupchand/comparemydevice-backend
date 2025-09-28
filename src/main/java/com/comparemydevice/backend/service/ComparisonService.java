// src/main/java/com/comparemydevice/backend/service/ComparisonService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;

import java.util.List;
import java.util.Map;

public interface ComparisonService {
    /**
     * Compare devices by their IDs.
     * @param deviceIds list of device IDs to compare
     * @return list of DeviceDTOs for comparison (with relations eager-loaded)
     */
    List<DeviceDTO> compareByIds(List<Long> deviceIds);

    /**
     * Compare devices grouped by specification categories (e.g. Display, Camera, Battery).
     * Returns a map from spec category (specType) to, for each deviceId, a
     * human-readable summary string like "Display Size: 6.7\" | Resolution: 2400x1080 | …".
     */
    Map<String, Map<Long, String>> compareSpecsByCategory(List<Long> deviceIds);

    /**
     * Validate that all devices belong to the same top-level category (e.g., all smartphones).
     * Returns true if valid; false otherwise.
     */
    boolean validateSameCategory(List<Long> deviceIds);
}