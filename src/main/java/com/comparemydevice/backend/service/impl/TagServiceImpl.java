// src/main/java/com/comparemydevice/backend/service/impl/TagServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.TagDTO;
import com.comparemydevice.backend.entity.Tag;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.TagRepository;
import com.comparemydevice.backend.service.TagService;
import com.comparemydevice.backend.service.support.SlugService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository repo;
    private final ModelMapper mapper;
    private final SlugService slugService = new SlugService();

    @Override @Transactional
    public TagDTO create(TagDTO dto) {
        Tag t = mapper.map(dto, Tag.class);
        t.setId(null);
        if (t.getSlug() == null || t.getSlug().isBlank()) {
            t.setSlug(slugService.ensureUnique(t.getName(), repo::existsBySlug));
        } else {
            t.setSlug(slugService.ensureUnique(t.getSlug(), repo::existsBySlug));
        }
        return toDTO(repo.save(t));
    }

    @Override
    public TagDTO get(Long id) { return toDTO(find(id)); }

    @Override @Transactional
    public TagDTO update(Long id, TagDTO dto) {
        Tag t = find(id);
        t.setName(dto.getName());
        if (dto.getSlug() != null && !dto.getSlug().isBlank() && !dto.getSlug().equals(t.getSlug())) {
            t.setSlug(slugService.ensureUnique(dto.getSlug(), repo::existsBySlug));
        }
        return toDTO(repo.save(t));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    @Override
    public List<TagDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private Tag find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + id));
    }
    private TagDTO toDTO(Tag t) { return mapper.map(t, TagDTO.class); }
}