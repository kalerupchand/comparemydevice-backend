// src/main/java/com/comparemydevice/backend/repository/DeviceRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsBySlug(String slug);
    Optional<Device> findBySlug(String slug);

    List<Device> findByBrand_Id(Long brandId);
    List<Device> findByCategory_Id(Long categoryId);
    List<Device> findByTags_Id(Long tagId);
    @Query("""
           SELECT d FROM Device d
           WHERE d.isDeleted = false
             AND (
                 LOWER(d.name)      LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(d.processor) LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(d.ram)       LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(d.storage)   LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(d.slug)      LIKE LOWER(CONCAT('%', :q, '%'))
             )
           """)
    Page<Device> search(@Param("q") String q, Pageable pageable);
}