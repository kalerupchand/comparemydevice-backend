package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.SpecKeyDTO;

import java.util.List;

public interface SpecKeyService {
    SpecKeyDTO create(SpecKeyDTO dto);
    SpecKeyDTO get(Long id);
    SpecKeyDTO update(Long id, SpecKeyDTO dto);
    void delete(Long id);
    List<SpecKeyDTO> getAll();

    // Lookups / filters
    SpecKeyDTO findByName(String name);
    List<SpecKeyDTO> listByType(String specType);
}