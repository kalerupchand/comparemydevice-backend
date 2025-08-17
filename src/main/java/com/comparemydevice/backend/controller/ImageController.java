package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.ImageDTO;
import com.comparemydevice.backend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing device images.
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Upload an image file for a device.
     */
    @PostMapping("/upload/{deviceId}")
    public ResponseEntity<ImageDTO> uploadImage(
            @PathVariable Long deviceId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText", required = false) String altText
    ) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(deviceId, file, altText));
    }

    /**
     * Add image directly from a URL (used for CSV import or admin panels).
     */
    @PostMapping("/url/{deviceId}")
    public ResponseEntity<ImageDTO> addImageFromUrl(
            @PathVariable Long deviceId,
            @RequestParam String url,
            @RequestParam(required = false) String altText,
            @RequestParam(defaultValue = "false") boolean isPrimary
    ) {
        return ResponseEntity.ok(imageService.addImageFromUrl(deviceId, url, altText, isPrimary));
    }

    /**
     * Get all images for a device.
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<ImageDTO>> getImagesByDeviceId(@PathVariable Long deviceId) {
        return ResponseEntity.ok(imageService.getImagesByDeviceId(deviceId));
    }

    /**
     * Delete image by ID.
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}