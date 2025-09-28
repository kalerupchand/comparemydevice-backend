package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
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
    public List<DeviceDTO> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId
    ) {
        return service.listFiltered(q, brandId, categoryId, tagId);
    }

    @GetMapping("/search")
    public Page<DeviceDTO> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        return service.search(q, brandId, categoryId, tagId, pageable);
    }

    @GetMapping("/compare")
    public List<DeviceDTO> compare(@RequestParam("ids") String idsCsv) {
        List<Long> ids = Arrays.stream(idsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Long::valueOf)
                .toList();
        return service.findByIds(ids);
    }

    @GetMapping("/by-brand/{brandId}")
    public List<DeviceDTO> byBrand(@PathVariable Long brandId) { return service.findByBrand(brandId); }

    @GetMapping("/by-category/{categoryId}")
    public List<DeviceDTO> byCategory(@PathVariable Long categoryId) { return service.findByCategory(categoryId); }

    @GetMapping("/by-tag/{tagId}")
    public List<DeviceDTO> byTag(@PathVariable Long tagId) { return service.findByTag(tagId); }

    @GetMapping("/search/suggestions")
    public ResponseEntity<Map<String, List<String>>> getSuggestions(@RequestParam("q") String query) {
        List<String> suggestions = service.getSearchSuggestions(query);
        return ResponseEntity.ok(Map.of("suggestions", suggestions));
    }
}