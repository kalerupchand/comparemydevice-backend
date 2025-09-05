// src/main/java/com/comparemydevice/backend/controller/DeviceController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService service;

    @PostMapping
    public ResponseEntity<DeviceDTO> create(@RequestBody DeviceDTO dto) {
        DeviceDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/devices/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public DeviceDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<DeviceDTO> getAll() { return service.getAll(); }

    @PutMapping("/{id}")
    public DeviceDTO update(@PathVariable Long id, @RequestBody DeviceDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/by-brand/{brandId}")
    public List<DeviceDTO> byBrand(@PathVariable Long brandId) { return service.findByBrand(brandId); }

    @GetMapping("/by-category/{categoryId}")
    public List<DeviceDTO> byCategory(@PathVariable Long categoryId) { return service.findByCategory(categoryId); }

    @GetMapping("/by-tag/{tagId}")
    public List<DeviceDTO> byTag(@PathVariable Long tagId) { return service.findByTag(tagId); }

    // src/main/java/com/comparemydevice/backend/controller/DeviceController.java

    @GetMapping("/search")
    public org.springframework.data.domain.Page<DeviceDTO> search(
            @RequestParam("q") String q,
            @org.springframework.data.web.PageableDefault(size = 20, sort = "name") org.springframework.data.domain.Pageable pageable
    ) {
        return service.search(q, pageable);
    }
}