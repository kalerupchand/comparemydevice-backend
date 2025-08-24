// src/main/java/com/comparemydevice/backend/service/impl/DeviceSpecServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.DeviceSpecDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.entity.DeviceSpec;
import com.comparemydevice.backend.entity.SpecKey;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.repository.DeviceSpecRepository;
import com.comparemydevice.backend.repository.SpecKeyRepository;
import com.comparemydevice.backend.service.DeviceSpecService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class DeviceSpecServiceImpl implements DeviceSpecService {

    private final DeviceSpecRepository repo;
    private final DeviceRepository deviceRepo;
    private final SpecKeyRepository specKeyRepo;
    private final ModelMapper mapper;

    @Override @Transactional
    public DeviceSpecDTO create(DeviceSpecDTO dto) {
        Device device = requireDevice(dto.getDeviceId());
        SpecKey key = requireSpecKey(dto.getSpecKeyId());

        DeviceSpec spec = new DeviceSpec();
        spec.setDevice(device);
        spec.setSpecKey(key);
        spec.setValueText(dto.getValueText());

        try {
            return toDTO(repo.save(spec));
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Specification already exists for device and spec key", ex);
        }
    }

    @Override
    public DeviceSpecDTO get(Long id) { return toDTO(find(id)); }

    @Override
    public List<DeviceSpecDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional
    public DeviceSpecDTO update(Long id, DeviceSpecDTO dto) {
        DeviceSpec spec = find(id);
        if (dto.getValueText() != null) spec.setValueText(dto.getValueText());
        return toDTO(repo.save(spec));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    private DeviceSpec find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DeviceSpec not found: " + id));
    }
    private Device requireDevice(Long id) {
        if (id == null) throw new IllegalArgumentException("deviceId is required");
        return deviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));
    }
    private SpecKey requireSpecKey(Long id) {
        if (id == null) throw new IllegalArgumentException("specKeyId is required");
        return specKeyRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SpecKey not found: " + id));
    }

    private DeviceSpecDTO toDTO(DeviceSpec s) {
        DeviceSpecDTO dto = mapper.map(s, DeviceSpecDTO.class);
        dto.setDeviceId(s.getDevice() != null ? s.getDevice().getId() : null);
        dto.setSpecKeyId(s.getSpecKey() != null ? s.getSpecKey().getId() : null);
        dto.setSpecKeyName(s.getSpecKey() != null ? s.getSpecKey().getName() : null);
        dto.setSpecType(s.getSpecKey() != null ? s.getSpecKey().getSpecType() : null);
        dto.setValueText(s.getValueText());
        return dto;
    }
}