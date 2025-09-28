package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO get(Long id);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);

    // Lists
    List<CategoryDTO> getAll();              // alphabetical
    Page<CategoryDTO> list(Pageable pageable); // paged

    // Lookups
    CategoryDTO getBySlug(String slug);
    boolean existsBySlug(String slug);
}