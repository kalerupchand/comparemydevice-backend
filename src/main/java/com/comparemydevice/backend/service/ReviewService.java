// src/main/java/com/comparemydevice/backend/service/ReviewService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO create(ReviewDTO dto);
    ReviewDTO get(Long id);
    ReviewDTO update(Long id, ReviewDTO dto);
    void delete(Long id);
    List<ReviewDTO> getAll();
}