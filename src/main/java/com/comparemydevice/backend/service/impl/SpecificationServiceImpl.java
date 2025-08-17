package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.SpecificationDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.entity.Specification;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.repository.SpecificationRepository;
import com.comparemydevice.backend.service.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the SpecificationService interface.
 * Handles business logic related to device specifications.
 */
@Service
@RequiredArgsConstructor
public class SpecificationServiceImpl implements SpecificationService {

    private final SpecificationRepository specificationRepository;
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;

    @Override
    public SpecificationDTO createSpecification(Long deviceId, SpecificationDTO specificationDTO) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));

        Specification specification = modelMapper.map(specificationDTO, Specification.class);
        specification.setDevice(device);
        specification.setCreatedAt(LocalDateTime.now());
        specification.setUpdatedAt(LocalDateTime.now());
        Specification savedSpec = specificationRepository.save(specification);

        return modelMapper.map(savedSpec, SpecificationDTO.class);
    }

    @Override
    public List<SpecificationDTO> getSpecificationsByDeviceId(Long deviceId) {
        List<Specification> specs = specificationRepository.findByDeviceId(deviceId);
        return specs.stream()
                .map(spec -> modelMapper.map(spec, SpecificationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SpecificationDTO updateSpecification(Long specId, SpecificationDTO specificationDTO) {
        Specification existingSpec = specificationRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("Specification", "id", specId));

        existingSpec.setKey(specificationDTO.getKey());
        existingSpec.setValue(specificationDTO.getValue());
        existingSpec.setSpecType(specificationDTO.getSpecType());
        existingSpec.setUpdatedAt(LocalDateTime.now());
        Specification updatedSpec = specificationRepository.save(existingSpec);
        return modelMapper.map(updatedSpec, SpecificationDTO.class);
    }

    @Override
    public void deleteSpecification(Long specId) {
        Specification spec = specificationRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("Specification", "id", specId));
        specificationRepository.delete(spec);
    }
}