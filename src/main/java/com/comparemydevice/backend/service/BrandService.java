package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.BrandDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    BrandDTO create(BrandDTO dto);
    BrandDTO get(Long id);
    BrandDTO update(Long id, BrandDTO dto);
    void delete(Long id);

    // Lists
    List<BrandDTO> getAll();            // alphabetical
    Page<BrandDTO> list(Pageable pageable); // paged

    // Lookups
    BrandDTO getBySlug(String slug);
    boolean existsBySlug(String slug);
}