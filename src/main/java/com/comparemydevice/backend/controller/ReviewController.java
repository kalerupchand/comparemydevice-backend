package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.ReviewDTO;
import com.comparemydevice.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing reviews for devices.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Create a new review for a device.
     */
    @PostMapping("/{deviceId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long deviceId, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.createReview(deviceId, reviewDTO));
    }

    /**
     * Get all reviews for a device.
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByDeviceId(@PathVariable Long deviceId) {
        return ResponseEntity.ok(reviewService.getReviewsByDeviceId(deviceId));
    }

    /**
     * Update a review by ID.
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewDTO));
    }

    /**
     * Delete a review by ID.
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}