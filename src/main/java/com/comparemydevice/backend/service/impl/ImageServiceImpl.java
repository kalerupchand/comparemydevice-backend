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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;

    @Override
    public ImageDTO uploadImage(Long deviceId, MultipartFile file, String altText) throws IOException {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + deviceId));

        // For now, we're using original filename as dummy "URL" (replace with actual file storage logic)
        String imageUrl = "/images/" + file.getOriginalFilename();

        Image image = Image.builder()
                .device(device)
                .url(imageUrl)
                .altText(altText)
                .isPrimary(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return modelMapper.map(imageRepository.save(image), ImageDTO.class);
    }

    @Override
    public ImageDTO addImageFromUrl(Long deviceId, String url, String altText, boolean isPrimary) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + deviceId));

        Image image = Image.builder()
                .device(device)
                .url(url)
                .altText(altText)
                .isPrimary(isPrimary)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return modelMapper.map(imageRepository.save(image), ImageDTO.class);
    }

    @Override
    public List<ImageDTO> getImagesByDeviceId(Long deviceId) {
        List<Image> images = imageRepository.findByDeviceId(deviceId);
        return images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteImage(Long imageId) {
        if (!imageRepository.existsById(imageId)) {
            throw new ResourceNotFoundException("Image not found with id: " + imageId);
        }
        imageRepository.deleteById(imageId);
    }
}