package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.repository.DeviceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class DeviceController {

    private final DeviceRepository repository;

    public DeviceController(DeviceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return repository.findAll();
    }

    @PostMapping
    public Device addDevice(@RequestBody Device device) {
        return repository.save(device);
    }
}