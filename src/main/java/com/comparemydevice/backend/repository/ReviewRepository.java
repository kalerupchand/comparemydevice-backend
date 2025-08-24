// src/main/java/com/comparemydevice/backend/repository/ReviewRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDevice_Id(Long deviceId);
}