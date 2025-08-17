package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.*;
import com.comparemydevice.backend.entity.*;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.*;
import com.comparemydevice.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public DeviceDTO createDevice(DeviceDTO deviceDTO) {
        Device device = modelMapper.map(deviceDTO, Device.class);
        device.setBrand(getBrand(deviceDTO.getBrand().getId()));
        device.setCategory(getCategory(deviceDTO.getCategory().getId()));
        device.setCreatedAt(LocalDateTime.now());
        device.setUpdatedAt(LocalDateTime.now()); // during creation

        return modelMapper.map(deviceRepository.save(device), DeviceDTO.class);
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(device -> modelMapper.map(device, DeviceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        return modelMapper.map(device, DeviceDTO.class);
    }

    @Override
    public DeviceDTO updateDevice(Long id, DeviceDTO deviceDTO) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));

        modelMapper.map(deviceDTO, device);
        device.setBrand(getBrand(deviceDTO.getBrand().getId()));
        device.setCategory(getCategory(deviceDTO.getCategory().getId()));
        device.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(deviceRepository.save(device), DeviceDTO.class);
    }

    @Override
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    private Brand getBrand(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}