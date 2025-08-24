// ImageController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.ImageDTO;
import com.comparemydevice.backend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/images") @RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    @PostMapping public ResponseEntity<ImageDTO> create(@RequestBody ImageDTO dto){ return ResponseEntity.ok(service.create(dto)); }
    @GetMapping("/{id}") public ResponseEntity<ImageDTO> get(@PathVariable Long id){ return ResponseEntity.ok(service.get(id)); }
    @GetMapping public ResponseEntity<List<ImageDTO>> getAll(){ return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}") public ResponseEntity<ImageDTO> update(@PathVariable Long id, @RequestBody ImageDTO dto){ return ResponseEntity.ok(service.update(id,dto)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){ service.delete(id); return ResponseEntity.noContent().build(); }
}