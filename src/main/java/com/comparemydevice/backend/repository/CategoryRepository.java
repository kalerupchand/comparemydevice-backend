// src/main/java/com/comparemydevice/backend/repository/CategoryRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<Category> findBySlug(String slug);
}