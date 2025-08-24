// src/main/java/com/comparemydevice/backend/controller/TagController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.TagDTO;
import com.comparemydevice.backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @PostMapping
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO dto) {
        TagDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/tags/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public TagDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<TagDTO> getAll() { return service.getAll(); }

    @PutMapping("/{id}")
    public TagDTO update(@PathVariable Long id, @RequestBody TagDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}