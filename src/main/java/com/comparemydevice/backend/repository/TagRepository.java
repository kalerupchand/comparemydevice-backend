package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);

    Optional<Tag> findBySlug(String slug);
    Optional<Tag> findByNameIgnoreCase(String name);

    List<Tag> findAllByOrderByNameAsc();

    // lightweight suggestion query (DB-friendly)
    List<Tag> findTop10ByNameContainingIgnoreCaseOrderByNameAsc(String q);
}