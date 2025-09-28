// src/main/java/com/comparemydevice/backend/service/impl/ComparisonServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.DeviceDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.entity.DeviceSpec;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComparisonServiceImpl implements ComparisonService {
    private final DeviceRepository deviceRepo;
    private final ModelMapper mapper;

    @Override
    public List<DeviceDTO> compareByIds(List<Long> deviceIds) {
        validateIds(deviceIds);

        // Eager-load relations so the front-end has specs/images/reviews/etc.
        List<Device> found = deviceRepo.findDistinctByIdIn(deviceIds);

        ensureAllFound(deviceIds, found);

        return found.stream()
                .map(d -> mapper.map(d, DeviceDTO.class))
                .toList();
    }

    @Override
    public Map<String, Map<Long, String>> compareSpecsByCategory(List<Long> deviceIds) {
        validateIds(deviceIds);

        // Eager load to access deviceSpecs/specKey safely
        List<Device> devices = deviceRepo.findDistinctByIdIn(deviceIds);
        ensureAllFound(deviceIds, devices);

        // category(specType) -> (deviceId -> "SpecName: Value | SpecName2: Value2")
        Map<String, Map<Long, String>> result = new LinkedHashMap<>();

        for (Device device : devices) {
            Long devId = device.getId();

            // Group this device's specs by specType (category)
            Map<String, List<DeviceSpec>> byType = device.getDeviceSpecs().stream()
                    .collect(Collectors.groupingBy(
                            ds -> ds.getSpecKey() != null && ds.getSpecKey().getSpecType() != null
                                    ? ds.getSpecKey().getSpecType()
                                    : "Other",
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            for (Map.Entry<String, List<DeviceSpec>> e : byType.entrySet()) {
                String specType = e.getKey();
                List<DeviceSpec> specs = e.getValue();

                // Build a readable summary for this device under this category
                String summary = specs.stream()
                        .sorted(Comparator.comparing(ds -> ds.getSpecKey() != null ? ds.getSpecKey().getName() : ""))
                        .map(ds -> {
                            String name = ds.getSpecKey() != null ? ds.getSpecKey().getName() : "Spec";
                            String val  = ds.getValueText() != null ? ds.getValueText() : "";
                            return name + ": " + val;
                        })
                        .collect(Collectors.joining(" | "));

                result.computeIfAbsent(specType, k -> new LinkedHashMap<>())
                        .put(devId, summary);
            }
        }

        // Ensure every category contains an entry for every requested deviceId (even if empty)
        for (Map<Long, String> perDevice : result.values()) {
            for (Long id : deviceIds) {
                perDevice.putIfAbsent(id, "");
            }
        }

        return result;
    }

    @Override
    public boolean validateSameCategory(List<Long> deviceIds) {
        validateIds(deviceIds);

        List<Device> devices = deviceRepo.findDistinctByIdIn(deviceIds);
        ensureAllFound(deviceIds, devices);

        Set<Long> categoryIds = devices.stream()
                .map(d -> d.getCategory() != null ? d.getCategory().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // If there's 0 or 1 distinct category IDs across the devices, they're "same category"
        return categoryIds.size() <= 1;
    }

    // ---- helpers ----

    private static void validateIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("deviceIds must not be empty");
        }
    }

    private static void ensureAllFound(List<Long> wantedOrder, List<Device> found) {
        Set<Long> wanted = new HashSet<>(wantedOrder);
        Set<Long> have = found.stream().map(Device::getId).collect(Collectors.toSet());
        if (have.size() != wanted.size()) {
            wanted.removeAll(have);
            throw new ResourceNotFoundException("Device(s) not found: " + wanted);
        }
    }
}