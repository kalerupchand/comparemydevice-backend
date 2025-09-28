package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.BrandDTO;
import com.comparemydevice.backend.entity.Brand;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.BrandRepository;
import com.comparemydevice.backend.service.BrandService;
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
public class BrandServiceImpl implements BrandService {

    private final BrandRepository repo;
    private final ModelMapper mapper;
    private final SlugService slugService = new SlugService();

    // ---------- CRUD ----------

    @Override
    @Transactional
    public BrandDTO create(BrandDTO dto) {
        Brand b = mapper.map(dto, Brand.class);
        b.setId(null);

        String baseSlug = (b.getSlug() == null || b.getSlug().isBlank()) ? b.getName() : b.getSlug();
        b.setSlug(slugService.ensureUnique(baseSlug, repo::existsBySlug));

        return toDTO(repo.save(b));
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDTO get(Long id) {
        return toDTO(find(id));
    }

    @Override
    @Transactional
    public BrandDTO update(Long id, BrandDTO dto) {
        Brand b = find(id);
        if (dto.getName() != null) b.setName(dto.getName());
        if (dto.getLogoUrl() != null) b.setLogoUrl(dto.getLogoUrl());

        if (dto.getSlug() != null) {
            String incoming = dto.getSlug().trim();
            if (incoming.isEmpty()) {
                // recompute from current name
                b.setSlug(slugService.ensureUnique(b.getName(), repo::existsBySlug));
            } else if (!incoming.equals(b.getSlug())) {
                b.setSlug(slugService.ensureUnique(incoming, repo::existsBySlug));
            }
        } else if (b.getSlug() == null || b.getSlug().isBlank()) {
            b.setSlug(slugService.ensureUnique(b.getName(), repo::existsBySlug));
        }

        return toDTO(repo.save(b));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    // ---------- Lists ----------

    @Override
    @Transactional(readOnly = true)
    public List<BrandDTO> getAll() {
        return repo.findAllByOrderByNameAsc().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandDTO> list(Pageable pageable) {
        return repo.findAll(pageable).map(this::toDTO);
    }

    // ---------- Lookups ----------

    @Override
    @Transactional(readOnly = true)
    public BrandDTO getBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("slug must not be blank");
        }
        Brand b = repo.findBySlug(slug.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found by slug: " + slug));
        return toDTO(b);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(String slug) {
        if (slug == null || slug.isBlank()) return false;
        return repo.existsBySlug(slug.trim());
    }

    // ---------- Helpers ----------

    private Brand find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + id));
    }

    private BrandDTO toDTO(Brand b) { return mapper.map(b, BrandDTO.class); }
}