// src/main/java/com/comparemydevice/backend/repository/TagRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<Tag> findBySlug(String slug);
}