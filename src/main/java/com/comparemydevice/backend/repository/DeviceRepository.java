// src/main/java/com/comparemydevice/backend/repository/DeviceRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsBySlug(String slug);
    Optional<Device> findBySlug(String slug);

    List<Device> findByBrand_Id(Long brandId);
    List<Device> findByCategory_Id(Long categoryId);
    List<Device> findByTags_Id(Long tagId);
}