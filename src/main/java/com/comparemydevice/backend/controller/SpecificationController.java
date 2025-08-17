package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.SpecificationDTO;
import com.comparemydevice.backend.service.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing specifications of devices.
 */
@RestController
@RequestMapping("/api/specifications")
@RequiredArgsConstructor
public class SpecificationController {

    private final SpecificationService specificationService;

    /**
     * Add specification to a specific device.
     */
    @PostMapping("/{deviceId}")
    public ResponseEntity<SpecificationDTO> createSpecification(
            @PathVariable Long deviceId,
            @RequestBody SpecificationDTO specificationDTO) {
        return ResponseEntity.ok(specificationService.createSpecification(deviceId, specificationDTO));
    }

    /**
     * Get all specifications for a specific device.
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<SpecificationDTO>> getSpecificationsByDeviceId(@PathVariable Long deviceId) {
        return ResponseEntity.ok(specificationService.getSpecificationsByDeviceId(deviceId));
    }

    /**
     * Update a specification by its ID.
     */
    @PutMapping("/{specId}")
    public ResponseEntity<SpecificationDTO> updateSpecification(
            @PathVariable Long specId,
            @RequestBody SpecificationDTO specificationDTO) {
        return ResponseEntity.ok(specificationService.updateSpecification(specId, specificationDTO));
    }

    /**
     * Delete a specification by its ID.
     */
    @DeleteMapping("/{specId}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long specId) {
        specificationService.deleteSpecification(specId);
        return ResponseEntity.noContent().build();
    }
}