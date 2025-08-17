package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.ReviewDTO;

import java.util.List;

/**
 * Service interface for managing reviews related to devices.
 * Handles creation, retrieval, update, and deletion of device reviews.
 */
public interface ReviewService {

    /**
     * Creates a new review for a specific device.
     *
     * @param deviceId the ID of the device to associate the review with
     * @param reviewDTO the review details
     * @return the created ReviewDTO
     */
    ReviewDTO createReview(Long deviceId, ReviewDTO reviewDTO);

    /**
     * Retrieves all reviews for a specific device.
     *
     * @param deviceId the ID of the device
     * @return a list of ReviewDTOs
     */
    List<ReviewDTO> getReviewsByDeviceId(Long deviceId);

    /**
     * Updates an existing review by its ID.
     *
     * @param reviewId the ID of the review to update
     * @param reviewDTO updated review data
     * @return the updated ReviewDTO
     */
    ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO);

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId the ID of the review to delete
     */
    void deleteReview(Long reviewId);
}