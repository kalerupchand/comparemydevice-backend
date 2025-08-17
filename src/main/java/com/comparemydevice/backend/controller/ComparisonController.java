package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for comparing devices.
 */
@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;

    /**
     * Compare two devices by their IDs.
     */
    @GetMapping("/{id1}/{id2}")
    public ResponseEntity<List<DeviceDTO>> compareTwoDevices(@PathVariable Long id1, @PathVariable Long id2) {
        return ResponseEntity.ok(comparisonService.compareDevices(id1, id2));
    }

    /**
     * Compare multiple devices using a list of IDs.
     */
    @PostMapping("/multiple")
    public ResponseEntity<List<DeviceDTO>> compareMultipleDevices(@RequestBody List<Long> deviceIds) {
        return ResponseEntity.ok(comparisonService.compareMultipleDevices(deviceIds));
    }
}