
// File: com.comparemydevice.backend.service.DeviceService.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
