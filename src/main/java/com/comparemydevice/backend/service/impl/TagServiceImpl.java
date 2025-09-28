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

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository repo;
    private final ModelMapper mapper;
    private final SlugService slugService = new SlugService();

    // -------------------- Create --------------------
    @Override
    @Transactional
    public TagDTO create(TagDTO dto) {
        String name = requireNonBlank(trimOrNull(dto.getName()), "name");
        String slug = trimOrNull(dto.getSlug());

        Tag t = new Tag();
        t.setId(null);
        t.setName(name);
        // ensure unique slug (from given slug or derived from name)
        t.setSlug(slugService.ensureUnique(slug != null ? slug : name, repo::existsBySlug));

        return toDTO(repo.save(t));
    }

    // -------------------- Read --------------------
    @Override
    @Transactional(readOnly = true)
    public TagDTO get(Long id) {
        return toDTO(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> getAll() {
        return repo.findAllByOrderByNameAsc().stream().map(this::toDTO).toList();
    }

    // -------------------- Update --------------------
    @Override
    @Transactional
    public TagDTO update(Long id, TagDTO dto) {
        Tag t = find(id);

        if (dto.getName() != null) {
            t.setName(requireNonBlank(trimOrNull(dto.getName()), "name"));
        }

        if (dto.getSlug() != null) {
            String incoming = trimOrNull(dto.getSlug());
            if (incoming == null) {
                // recompute from current name
                t.setSlug(slugService.ensureUnique(t.getName(), repo::existsBySlug));
            } else if (!incoming.equals(t.getSlug())) {
                t.setSlug(slugService.ensureUnique(incoming, repo::existsBySlug));
            }
        }

        return toDTO(repo.save(t));
    }

    // -------------------- Delete --------------------
    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    // -------------------- New lookups & suggestions --------------------
    @Override
    @Transactional(readOnly = true)
    public TagDTO findByName(String name) {
        String n = requireNonBlank(trimOrNull(name), "name");
        Tag t = repo.findByNameIgnoreCase(n)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found by name: " + n));
        return toDTO(t);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO findBySlug(String slug) {
        String s = requireNonBlank(trimOrNull(slug), "slug");
        Tag t = repo.findBySlug(s)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found by slug: " + s));
        return toDTO(t);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> searchSuggestions(String query) {
        String q = trimOrNull(query);
        if (q == null) return List.of();
        return repo.findTop10ByNameContainingIgnoreCaseOrderByNameAsc(q)
                .stream().map(Tag::getName).toList();
    }

    // -------------------- Helpers --------------------
    private Tag find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + id));
    }

    private TagDTO toDTO(Tag t) {
        return mapper.map(t, TagDTO.class);
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