// src/main/java/com/comparemydevice/backend/service/impl/ComparisonServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService {
    private final DeviceRepository deviceRepo;
    private final ModelMapper mapper;

    @Override
    public List<DeviceDTO> compareByIds(List<Long> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) {
            throw new IllegalArgumentException("deviceIds must not be empty");
        }
        Set<Long> wanted = deviceIds.stream().collect(Collectors.toSet());
        List<Device> found = deviceRepo.findAllById(wanted);
        if (found.size() != wanted.size()) {
            Set<Long> have = found.stream().map(Device::getId).collect(Collectors.toSet());
            wanted.removeAll(have);
            throw new ResourceNotFoundException("Device(s) not found: " + wanted);
        }
        return found.stream().map(d -> mapper.map(d, DeviceDTO.class)).collect(Collectors.toList());
    }
}