// src/main/java/com/comparemydevice/backend/service/impl/ReviewServiceImpl.java
package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.ReviewDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.entity.Review;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.repository.ReviewRepository;
import com.comparemydevice.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repo;
    private final DeviceRepository deviceRepo;
    private final ModelMapper mapper;

    @Override @Transactional
    public ReviewDTO create(ReviewDTO dto) {
        validateRating(dto.getRating());
        Device device = deviceRepo.findById(dto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + dto.getDeviceId()));

        Review r = mapper.map(dto, Review.class);
        r.setId(null);
        r.setDevice(device);
        return toDTO(repo.save(r));
    }

    @Override
    public ReviewDTO get(Long id) { return toDTO(find(id)); }

    @Override
    public List<ReviewDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional
    public ReviewDTO update(Long id, ReviewDTO dto) {
        Review r = find(id);
        if (dto.getReviewerName() != null) r.setReviewerName(dto.getReviewerName());
        if (dto.getContent() != null) r.setContent(dto.getContent());
        if (dto.getSourceUrl() != null) r.setSourceUrl(dto.getSourceUrl());
        if (dto.getRating() != null) {
            validateRating(dto.getRating());
            r.setRating(dto.getRating());
        }
        return toDTO(repo.save(r));
    }

    @Override @Transactional
    public void delete(Long id) { repo.delete(find(id)); }

    private void validateRating(BigDecimal rating) {
        if (rating == null) return;
        if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Rating must be in [0..5]");
        }
    }

    private Review find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found: " + id));
    }
    private ReviewDTO toDTO(Review r) { return mapper.map(r, ReviewDTO.class); }
}