// src/main/java/com/comparemydevice/backend/service/impl/SpecKeyServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.SpecKeyDTO;
import com.comparemydevice.backend.entity.SpecKey;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.SpecKeyRepository;
import com.comparemydevice.backend.service.SpecKeyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class SpecKeyServiceImpl implements SpecKeyService {
    private final SpecKeyRepository repo;
    private final ModelMapper mapper;

    @Override @Transactional
    public SpecKeyDTO create(SpecKeyDTO dto) {
        SpecKey k = mapper.map(dto, SpecKey.class);
        k.setId(null);
        return toDTO(repo.save(k));
    }

    @Override
    public SpecKeyDTO get(Long id) { return toDTO(find(id)); }

    @Override @Transactional
    public SpecKeyDTO update(Long id, SpecKeyDTO dto) {
        SpecKey k = find(id);
        k.setName(dto.getName());
        k.setSpecType(dto.getSpecType());
        return toDTO(repo.save(k));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    @Override
    public List<SpecKeyDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private SpecKey find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SpecKey not found: " + id));
    }
    private SpecKeyDTO toDTO(SpecKey k) { return mapper.map(k, SpecKeyDTO.class); }
}