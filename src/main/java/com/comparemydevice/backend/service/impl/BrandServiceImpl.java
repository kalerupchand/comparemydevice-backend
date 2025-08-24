// src/main/java/com/comparemydevice/backend/service/impl/BrandServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.BrandDTO;
import com.comparemydevice.backend.entity.Brand;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.BrandRepository;
import com.comparemydevice.backend.service.BrandService;
import com.comparemydevice.backend.service.support.SlugService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository repo;
    private final ModelMapper mapper;
    private final SlugService slugService = new SlugService();

    @Override @Transactional
    public BrandDTO create(BrandDTO dto) {
        Brand b = mapper.map(dto, Brand.class);
        b.setId(null);
        if (b.getSlug() == null || b.getSlug().isBlank()) {
            b.setSlug(slugService.ensureUnique(b.getName(), repo::existsBySlug));
        } else {
            b.setSlug(slugService.ensureUnique(b.getSlug(), repo::existsBySlug));
        }
        return toDTO(repo.save(b));
    }

    @Override
    public BrandDTO get(Long id) { return toDTO(find(id)); }

    @Override @Transactional
    public BrandDTO update(Long id, BrandDTO dto) {
        Brand b = find(id);
        b.setName(dto.getName());
        b.setLogoUrl(dto.getLogoUrl());
        if (dto.getSlug() != null && !dto.getSlug().isBlank() && !dto.getSlug().equals(b.getSlug())) {
            b.setSlug(slugService.ensureUnique(dto.getSlug(), repo::existsBySlug));
        }
        return toDTO(repo.save(b));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    @Override
    public List<BrandDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private Brand find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + id));
    }

    private BrandDTO toDTO(Brand b) { return mapper.map(b, BrandDTO.class); }
}