package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.ImageDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.entity.Image;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.repository.ImageRepository;
import com.comparemydevice.backend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repo;
    private final DeviceRepository deviceRepo;
    private final ModelMapper mapper;

    // -------------------- Create --------------------
    @Override
    @Transactional
    public ImageDTO create(ImageDTO dto) {
        Device device = deviceRepo.findById(dto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + dto.getDeviceId()));

        Image img = mapper.map(dto, Image.class);
        img.setId(null);
        img.setDevice(device);

        // Enforce single primary image per device
        if (Boolean.TRUE.equals(img.getIsPrimary()) &&
                repo.existsByDevice_IdAndIsPrimaryTrue(device.getId())) {
            throw new IllegalArgumentException("Device already has a primary image");
        }

        return toDTO(repo.save(img));
    }

    // -------------------- Read --------------------
    @Override
    @Transactional(readOnly = true)
    public ImageDTO get(Long id) {
        return toDTO(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDTO> listByDevice(Long deviceId) {
        return repo.findByDevice_IdOrderBySortOrderAscIdAsc(deviceId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ImageDTO getPrimaryImage(Long deviceId) {
        Image primary = repo.findFirstByDevice_IdAndIsPrimaryTrue(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Primary image not found for device: " + deviceId));
        return toDTO(primary);
    }

    // -------------------- Update --------------------
    @Override
    @Transactional
    public ImageDTO update(Long id, ImageDTO dto) {
        Image img = find(id);

        if (dto.getUrl() != null) img.setUrl(dto.getUrl());
        if (dto.getAltText() != null) img.setAltText(dto.getAltText());
        if (dto.getSortOrder() != null) img.setSortOrder(dto.getSortOrder());

        // Handle primary flag changes
        if (dto.getIsPrimary() != null) {
            if (dto.getIsPrimary()) {
                // If setting this image as primary, unset any other current primary
                repo.findFirstByDevice_IdAndIsPrimaryTrue(img.getDevice().getId())
                        .filter(existing -> !existing.getId().equals(img.getId()))
                        .ifPresent(existing -> {
                            existing.setIsPrimary(false);
                            repo.save(existing);
                        });
                img.setIsPrimary(true);
            } else {
                img.setIsPrimary(false);
            }
        }

        return toDTO(repo.save(img));
    }

    // -------------------- Delete --------------------
    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    // -------------------- Helpers --------------------
    private Image find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + id));
    }

    private ImageDTO toDTO(Image i) {
        ImageDTO dto = mapper.map(i, ImageDTO.class);
        dto.setDeviceId(i.getDevice() != null ? i.getDevice().getId() : null);
        return dto;
    }
}