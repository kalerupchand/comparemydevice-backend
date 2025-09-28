package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.DeviceSpecDTO;
import com.comparemydevice.backend.service.DeviceSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/device-specs")
@RequiredArgsConstructor
public class DeviceSpecController {

    private final DeviceSpecService service;

    // -------- CRUD --------

    @PostMapping
    public ResponseEntity<DeviceSpecDTO> create(@RequestBody DeviceSpecDTO dto) {
        DeviceSpecDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/device-specs/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public DeviceSpecDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<DeviceSpecDTO> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public DeviceSpecDTO update(@PathVariable Long id, @RequestBody DeviceSpecDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -------- Device-scoped listing --------

    /** Get all specifications for a specific device (sorted by spec key name). */
    @GetMapping("/device/{deviceId}")
    public List<DeviceSpecDTO> listByDevice(@PathVariable Long deviceId) {
        return service.listByDevice(deviceId);
    }
}