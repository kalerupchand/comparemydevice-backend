// src/main/java/com/comparemydevice/backend/repository/DeviceRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    boolean existsBySlug(String slug);

    List<Device> findByBrand_Id(Long brandId);
    List<Device> findByCategory_Id(Long categoryId);
    List<Device> findByTags_Id(Long tagId);

    /**
     * Unified finder with optional filters (q / brand / category / tag).
     * - DISTINCT avoids duplicates when joining tags.
     * - JPQL works across MySQL/H2.
     */
    @Query("""
           SELECT DISTINCT d
           FROM Device d
           LEFT JOIN d.tags t
           WHERE d.isDeleted = false
             AND (:brandId   IS NULL OR d.brand.id    = :brandId)
             AND (:categoryId IS NULL OR d.category.id = :categoryId)
             AND (:tagId     IS NULL OR t.id          = :tagId)
             AND (
                  :q IS NULL OR :q = '' OR
                  LOWER(d.name)      LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.processor) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.ram)       LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.storage)   LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(d.slug)      LIKE LOWER(CONCAT('%', :q, '%'))
             )
           """)
    Page<Device> searchAll(@Param("q") String q,
                           @Param("brandId") Long brandId,
                           @Param("categoryId") Long categoryId,
                           @Param("tagId") Long tagId,
                           Pageable pageable);
    @Query("SELECT d.name FROM Device d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY d.name ASC")
    List<String> findSuggestionsByName(@Param("query") String query);
}