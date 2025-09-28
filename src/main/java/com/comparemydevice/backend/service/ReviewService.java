package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO create(ReviewDTO dto);
    ReviewDTO get(Long id);
    ReviewDTO update(Long id, ReviewDTO dto);
    void delete(Long id);
    List<ReviewDTO> getAll();

    // Fetch reviews for a specific device (newest first)
    List<ReviewDTO> listByDevice(Long deviceId);
}