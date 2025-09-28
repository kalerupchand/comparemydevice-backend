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

@Service
@RequiredArgsConstructor
public class SpecKeyServiceImpl implements SpecKeyService {

    private final SpecKeyRepository repo;
    private final ModelMapper mapper;

    // -------- CRUD --------

    @Override
    @Transactional
    public SpecKeyDTO create(SpecKeyDTO dto) {
        SpecKey k = new SpecKey();
        k.setId(null);
        k.setName(requireNonBlank(trimOrNull(dto.getName()), "name"));
        k.setSpecType(trimOrNull(dto.getSpecType()));
        return toDTO(repo.save(k));
    }

    @Override
    @Transactional(readOnly = true)
    public SpecKeyDTO get(Long id) {
        return toDTO(find(id));
    }

    @Override
    @Transactional
    public SpecKeyDTO update(Long id, SpecKeyDTO dto) {
        SpecKey k = find(id);
        if (dto.getName() != null) {
            k.setName(requireNonBlank(trimOrNull(dto.getName()), "name"));
        }
        if (dto.getSpecType() != null) {
            k.setSpecType(trimOrNull(dto.getSpecType()));
        }
        return toDTO(repo.save(k));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecKeyDTO> getAll() {
        return repo.findAllByOrderByNameAsc().stream().map(this::toDTO).toList();
    }

    // -------- Lookups / filters --------

    @Override
    @Transactional(readOnly = true)
    public SpecKeyDTO findByName(String name) {
        String n = requireNonBlank(trimOrNull(name), "name");
        SpecKey k = repo.findByNameIgnoreCase(n)
                .orElseThrow(() -> new ResourceNotFoundException("SpecKey not found by name: " + n));
        return toDTO(k);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecKeyDTO> listByType(String specType) {
        String t = trimOrNull(specType);
        if (t == null) {
            // If no type provided, return all (or choose to return empty list)
            return getAll();
        }
        return repo.findBySpecTypeIgnoreCase(t).stream().map(this::toDTO).toList();
    }

    // -------- Helpers --------

    private SpecKey find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SpecKey not found: " + id));
    }

    private SpecKeyDTO toDTO(SpecKey k) {
        return mapper.map(k, SpecKeyDTO.class);
    }

    private static String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String requireNonBlank(String s, String field) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return s;
    }
}