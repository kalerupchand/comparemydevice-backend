// src/main/java/com/comparemydevice/backend/service/CategoryService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO get(Long id);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);
    List<CategoryDTO> getAll();
}