// src/main/java/com/comparemydevice/backend/repository/ImageRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByDevice_Id(Long deviceId);
    boolean existsByDevice_IdAndIsPrimaryTrue(Long deviceId);
}