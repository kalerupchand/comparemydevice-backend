package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO create(TagDTO dto);
    TagDTO get(Long id);
    TagDTO update(Long id, TagDTO dto);
    void delete(Long id);
    List<TagDTO> getAll();

    // New: lookups & suggestions
    TagDTO findByName(String name);
    TagDTO findBySlug(String slug);
    List<String> searchSuggestions(String query);
}