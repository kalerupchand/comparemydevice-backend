package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /** All reviews for a device, newest first. */
    List<Review> findByDevice_IdOrderByCreatedAtDesc(Long deviceId);

    /** (Optional) unsorted variant if you need it elsewhere. */
    List<Review> findByDevice_Id(Long deviceId);
}