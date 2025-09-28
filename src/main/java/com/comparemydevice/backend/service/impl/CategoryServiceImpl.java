package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.CategoryDTO;
import com.comparemydevice.backend.entity.Category;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.CategoryRepository;
import com.comparemydevice.backend.service.CategoryService;
import com.comparemydevice.backend.service.support.SlugService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;
    private final ModelMapper mapper;
    private final SlugService slugService = new SlugService();

    // ---------- CRUD ----------

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        Category c = mapper.map(dto, Category.class);
        c.setId(null);

        // Generate or normalize a unique slug
        String baseSlug = (c.getSlug() == null || c.getSlug().isBlank()) ? c.getName() : c.getSlug();
        c.setSlug(slugService.ensureUnique(baseSlug, repo::existsBySlug));

        return toDTO(repo.save(c));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO get(Long id) { return toDTO(find(id)); }

    @Override
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category c = find(id);

        if (dto.getName() != null)    c.setName(dto.getName());
        if (dto.getIconUrl() != null) c.setIconUrl(dto.getIconUrl());

        if (dto.getSlug() != null) {
            String incoming = dto.getSlug().trim();
            if (incoming.isEmpty()) {
                // recompute from current name
                c.setSlug(slugService.ensureUnique(c.getName(), repo::existsBySlug));
            } else if (!incoming.equals(c.getSlug())) {
                c.setSlug(slugService.ensureUnique(incoming, repo::existsBySlug));
            }
        } else if (c.getSlug() == null || c.getSlug().isBlank()) {
            c.setSlug(slugService.ensureUnique(c.getName(), repo::existsBySlug));
        }

        return toDTO(repo.save(c));
    }

    @Override
    @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    // ---------- Lists ----------

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAll() {
        return repo.findAllByOrderByNameAsc().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> list(Pageable pageable) {
        return repo.findAll(pageable).map(this::toDTO);
    }

    // ---------- Lookups ----------

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getBySlug(String slug) {
        if (slug == null || slug.isBlank()) throw new IllegalArgumentException("slug must not be blank");
        Category c = repo.findBySlug(slug.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found by slug: " + slug));
        return toDTO(c);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        if (slug == null || slug.isBlank()) return false;
        return repo.existsBySlug(slug.trim());
    }

    // ---------- Helpers ----------

    private Category find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private CategoryDTO toDTO(Category c) { return mapper.map(c, CategoryDTO.class); }
}