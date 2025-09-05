// src/main/java/com/comparemydevice/backend/repository/DeviceRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    // --- Simple filters used by list endpoint (no pagination) ---
    @Query("""
           SELECT DISTINCT d
           FROM Device d
           LEFT JOIN d.tags t
           WHERE d.isDeleted = false
             AND (:q IS NULL OR
                  LOWER(d.name)      LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.processor) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.ram)       LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.storage)   LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.slug)      LIKE LOWER(CONCAT('%', :q, '%')))
             AND (:brandId IS NULL OR d.brand.id = :brandId)
             AND (:categoryId IS NULL OR d.category.id = :categoryId)
             AND (:tagId IS NULL OR t.id = :tagId)
           """)
    List<Device> filter(
            @Param("q") String q,
            @Param("brandId") Long brandId,
            @Param("categoryId") Long categoryId,
            @Param("tagId") Long tagId
    );

    // --- Paginated search (used by /search) ---
    @Query("""
           SELECT DISTINCT d
           FROM Device d
           LEFT JOIN d.tags t
           WHERE d.isDeleted = false
             AND (:q IS NULL OR
                  LOWER(d.name)      LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.processor) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.ram)       LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.storage)   LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.slug)      LIKE LOWER(CONCAT('%', :q, '%')))
             AND (:brandId IS NULL OR d.brand.id = :brandId)
             AND (:categoryId IS NULL OR d.category.id = :categoryId)
             AND (:tagId IS NULL OR t.id = :tagId)
           """)
    Page<Device> search(
            @Param("q") String q,
            @Param("brandId") Long brandId,
            @Param("categoryId") Long categoryId,
            @Param("tagId") Long tagId,
            Pageable pageable
    );

    // (Existing convenience finders, if you still use them elsewhere)
    List<Device> findByBrand_Id(Long brandId);
    List<Device> findByCategory_Id(Long categoryId);
    List<Device> findByTags_Id(Long tagId);

    boolean existsBySlug(String slug);
}