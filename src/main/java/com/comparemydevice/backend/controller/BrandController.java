// src/main/java/com/comparemydevice/backend/controller/BrandController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.BrandDTO;
import com.comparemydevice.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService service;

    @PostMapping
    public ResponseEntity<BrandDTO> create(@RequestBody BrandDTO dto) {
        BrandDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/brands/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public BrandDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<BrandDTO> getAll() { return service.getAll(); }

    @PutMapping("/{id}")
    public BrandDTO update(@PathVariable Long id, @RequestBody BrandDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}