// src/main/java/com/comparemydevice/backend/controller/ComparisonController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/compare")
@RequiredArgsConstructor
public class ComparisonController {
    private final ComparisonService service;

    @PostMapping
    public List<DeviceDTO> compare(@RequestBody List<Long> deviceIds) {
        return service.compareByIds(deviceIds);
    }
}