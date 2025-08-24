// src/main/java/com/comparemydevice/backend/repository/BrandRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<Brand> findBySlug(String slug);
}