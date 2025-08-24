// src/main/java/com/comparemydevice/backend/controller/DeviceSpecController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceSpecDTO;
import com.comparemydevice.backend.service.DeviceSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/device-specs")
@RequiredArgsConstructor
public class DeviceSpecController {
    private final DeviceSpecService service;

    @PostMapping
    public ResponseEntity<DeviceSpecDTO> create(@RequestBody DeviceSpecDTO dto) {
        DeviceSpecDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/device-specs/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public DeviceSpecDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<DeviceSpecDTO> getAll() { return service.getAll(); }

    @PutMapping("/{id}")
    public DeviceSpecDTO update(@PathVariable Long id, @RequestBody DeviceSpecDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}