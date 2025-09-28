package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.BrandDTO;
import com.comparemydevice.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService service;

    // ---------- CRUD ----------

    @PostMapping
    public ResponseEntity<BrandDTO> create(@RequestBody BrandDTO dto) {
        BrandDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/brands/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public BrandDTO get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public BrandDTO update(@PathVariable Long id, @RequestBody BrandDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    // ---------- Lists ----------

    /** Non-paged: alphabetical for dropdowns, etc. */
    @GetMapping
    public List<BrandDTO> getAll() { return service.getAll(); }

    /** Paged list for admin screens: /api/brands/page?page=0&size=20&sort=name,asc */
    @GetMapping("/page")
    public Page<BrandDTO> list(Pageable pageable) { return service.list(pageable); }

    // ---------- Lookups / utilities ----------

    @GetMapping("/slug/{slug}")
    public BrandDTO getBySlug(@PathVariable String slug) { return service.getBySlug(slug); }

    /** Quick uniqueness check: /api/brands/exists?slug=apple */
    @GetMapping("/exists")
    public boolean existsBySlug(@RequestParam String slug) { return service.existsBySlug(slug); }
}