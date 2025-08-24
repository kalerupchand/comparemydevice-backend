// src/main/java/com/comparemydevice/backend/controller/SpecKeyController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.SpecKeyDTO;
import com.comparemydevice.backend.service.SpecKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/spec-keys")
@RequiredArgsConstructor
public class SpecKeyController {
    private final SpecKeyService service;

    @PostMapping
    public ResponseEntity<SpecKeyDTO> create(@RequestBody SpecKeyDTO dto) {
        SpecKeyDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/spec-keys/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public SpecKeyDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<SpecKeyDTO> getAll() { return service.getAll(); }

    @PutMapping("/{id}")
    public SpecKeyDTO update(@PathVariable Long id, @RequestBody SpecKeyDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}