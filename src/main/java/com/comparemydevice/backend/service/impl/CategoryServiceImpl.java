// src/main/java/com/comparemydevice/backend/service/impl/CategoryServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.CategoryDTO;
import com.comparemydevice.backend.entity.Category;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.CategoryRepository;
import com.comparemydevice.backend.service.CategoryService;
import com.comparemydevice.backend.service.support.SlugService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final ModelMapper mapper;
    private final SlugService slugService = new SlugService();

    @Override @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        Category c = mapper.map(dto, Category.class);
        c.setId(null);
        if (c.getSlug() == null || c.getSlug().isBlank()) {
            c.setSlug(slugService.ensureUnique(c.getName(), repo::existsBySlug));
        } else {
            c.setSlug(slugService.ensureUnique(c.getSlug(), repo::existsBySlug));
        }
        return toDTO(repo.save(c));
    }

    @Override
    public CategoryDTO get(Long id) { return toDTO(find(id)); }

    @Override @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category c = find(id);
        c.setName(dto.getName());
        c.setIconUrl(dto.getIconUrl());
        if (dto.getSlug() != null && !dto.getSlug().isBlank() && !dto.getSlug().equals(c.getSlug())) {
            c.setSlug(slugService.ensureUnique(dto.getSlug(), repo::existsBySlug));
        }
        return toDTO(repo.save(c));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    @Override
    public List<CategoryDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private Category find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private CategoryDTO toDTO(Category c) { return mapper.map(c, CategoryDTO.class); }
}