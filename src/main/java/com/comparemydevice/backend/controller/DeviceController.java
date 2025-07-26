// File: com.comparemydevice.backend.controller.DeviceController.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*") // Update origin for production
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @PostMapping
    public Device addDevice(@Valid @RequestBody Device device) {
        return deviceService.addDevice(device);
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceService.getDeviceById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }
}