// src/main/java/com/comparemydevice/backend/controller/ReviewController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.ReviewDTO;
import com.comparemydevice.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) {
        ReviewDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/reviews/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ReviewDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<ReviewDTO> getAll() { return service.getAll(); }

    @PutMapping("/{id}")
    public ReviewDTO update(@PathVariable Long id, @RequestBody ReviewDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}