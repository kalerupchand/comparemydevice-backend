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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceSpecServiceImpl implements DeviceSpecService {

    private final DeviceSpecRepository repo;
    private final DeviceRepository deviceRepo;
    private final SpecKeyRepository specKeyRepo;

    // -------------------- Create --------------------

    @Override
    @Transactional
    public DeviceSpecDTO create(DeviceSpecDTO dto) {
        Device device = requireDevice(dto.getDeviceId());
        SpecKey key = requireSpecKey(dto.getSpecKeyId());

        // Fail fast before hitting DB unique constraint
        if (repo.existsByDevice_IdAndSpecKey_Id(device.getId(), key.getId())) {
            throw new IllegalArgumentException("Specification already exists for the given device and spec key");
        }

        DeviceSpec spec = new DeviceSpec();
        spec.setDevice(device);
        spec.setSpecKey(key);
        spec.setValueText(dto.getValueText());

        try {
            return toDTO(repo.save(spec));
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Specification already exists for the given device and spec key", ex);
        }
    }

    // -------------------- Read --------------------

    @Override
    @Transactional(readOnly = true)
    public DeviceSpecDTO get(Long id) {
        return toDTO(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceSpecDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    /**
     * Load all specs for a device. Uses DeviceRepository#findWithRelationsById so
     * that associated collections are ready for serialization (no LAZY issues).
     */
    @Override
    @Transactional(readOnly = true)
    public List<DeviceSpecDTO> listByDevice(Long deviceId) {
        Device device = deviceRepo.findWithRelationsById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));

        return device.getDeviceSpecs().stream()
                .sorted(Comparator.comparing(ds -> ds.getSpecKey() != null ? ds.getSpecKey().getName() : ""))
                .map(this::toDTO)
                .toList();
    }

    // -------------------- Update --------------------

    @Override
    @Transactional
    public DeviceSpecDTO update(Long id, DeviceSpecDTO dto) {
        DeviceSpec spec = find(id);

        if (dto.getValueText() != null) {
            spec.setValueText(dto.getValueText());
        }
        // Not allowing device/specKey changes here to avoid duplicate pairs.

        return toDTO(repo.save(spec));
    }

    // -------------------- Delete --------------------

    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    // -------------------- Helpers --------------------

    private DeviceSpec find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeviceSpec not found: " + id));
    }

    private Device requireDevice(Long id) {
        if (id == null) throw new IllegalArgumentException("deviceId is required");
        return deviceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));
    }

    private SpecKey requireSpecKey(Long id) {
        if (id == null) throw new IllegalArgumentException("specKeyId is required");
        return specKeyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SpecKey not found: " + id));
    }

    private DeviceSpecDTO toDTO(DeviceSpec s) {
        return DeviceSpecDTO.builder()
                .id(s.getId())
                .deviceId(s.getDevice() != null ? s.getDevice().getId() : null)
                .specKeyId(s.getSpecKey() != null ? s.getSpecKey().getId() : null)
                .specKeyName(s.getSpecKey() != null ? s.getSpecKey().getName() : null)
                .specType(s.getSpecKey() != null ? s.getSpecKey().getSpecType() : null)
                .valueText(s.getValueText())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}