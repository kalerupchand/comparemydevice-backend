package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.*;
import com.comparemydevice.backend.entity.*;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ComparisonService interface for comparing devices.
 */
@Service
@RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService {

    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DeviceDTO> compareDevices(Long deviceId1, Long deviceId2) {
        return compareMultipleDevices(List.of(deviceId1, deviceId2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DeviceDTO> compareMultipleDevices(List<Long> deviceIds) {
        return deviceIds.stream()
                .map(id -> deviceRepository.findById(id)
                        .map(device -> modelMapper.map(device, DeviceDTO.class))
                        .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + id)))
                .collect(Collectors.toList());
    }
}