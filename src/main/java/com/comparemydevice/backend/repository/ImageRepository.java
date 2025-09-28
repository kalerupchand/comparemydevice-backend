package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // Get all images for a device, sorted by order
    List<Image> findByDevice_IdOrderBySortOrderAscIdAsc(Long deviceId);

    // Get the primary image if it exists
    Optional<Image> findFirstByDevice_IdAndIsPrimaryTrue(Long deviceId);

    // Check if a primary image already exists
    boolean existsByDevice_IdAndIsPrimaryTrue(Long deviceId);

    // Count total images for a device
    long countByDevice_Id(Long deviceId);
}