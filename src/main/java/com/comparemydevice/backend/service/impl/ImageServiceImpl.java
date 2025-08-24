// src/main/java/com/comparemydevice/backend/service/impl/ImageServiceImpl.java
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
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repo;
    private final DeviceRepository deviceRepo;
    private final ModelMapper mapper;

    @Override @Transactional
    public ImageDTO create(ImageDTO dto) {
        Device device = deviceRepo.findById(dto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + dto.getDeviceId()));
        Image img = mapper.map(dto, Image.class);
        img.setId(null);
        img.setDevice(device);
        // if H2 (dev) cannot enforce single primary image, prevent here (optional):
        if (Boolean.TRUE.equals(img.getIsPrimary()) && repo.existsByDevice_IdAndIsPrimaryTrue(device.getId())) {
            throw new IllegalArgumentException("Device already has a primary image");
        }
        return toDTO(repo.save(img));
    }

    @Override
    public ImageDTO get(Long id) { return toDTO(find(id)); }

    @Override
    public List<ImageDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional
    public ImageDTO update(Long id, ImageDTO dto) {
        Image img = find(id);
        if (dto.getUrl() != null) img.setUrl(dto.getUrl());
        if (dto.getAltText() != null) img.setAltText(dto.getAltText());
        if (dto.getSortOrder() != null) img.setSortOrder(dto.getSortOrder());
        if (dto.getIsPrimary() != null && dto.getIsPrimary() && !Boolean.TRUE.equals(img.getIsPrimary())) {
            if (repo.existsByDevice_IdAndIsPrimaryTrue(img.getDevice().getId())) {
                throw new IllegalArgumentException("Device already has a primary image");
            }
        }
        if (dto.getIsPrimary() != null) img.setIsPrimary(dto.getIsPrimary());
        return toDTO(repo.save(img));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    private Image find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found: " + id));
    }
    private ImageDTO toDTO(Image i) { return mapper.map(i, ImageDTO.class); }
}