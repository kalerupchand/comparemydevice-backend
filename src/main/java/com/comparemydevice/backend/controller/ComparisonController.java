// src/main/java/com/comparemydevice/backend/controller/ComparisonController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class ComparisonController {
    private final ComparisonService service;

    /** Basic device-to-device comparison (returns full DeviceDTOs). */
    @PostMapping
    public ResponseEntity<List<DeviceDTO>> compare(@RequestBody List<Long> deviceIds) {
        return ResponseEntity.ok(service.compareByIds(deviceIds));
    }

    /** Optional: compare specs grouped by spec categories (Display, Camera, Battery...). */
    @PostMapping("/specs-by-category")
    public ResponseEntity<Map<String, Map<Long, String>>> compareSpecsByCategory(@RequestBody List<Long> deviceIds) {
        return ResponseEntity.ok(service.compareSpecsByCategory(deviceIds));
    }

    /** Optional: validate that all devices belong to the same category (e.g., all smartphones). */
    @PostMapping("/validate-same-category")
    public ResponseEntity<Boolean> validateSameCategory(@RequestBody List<Long> deviceIds) {
        return ResponseEntity.ok(service.validateSameCategory(deviceIds));
    }
}