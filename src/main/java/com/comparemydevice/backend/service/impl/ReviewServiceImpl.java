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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repo;
    private final DeviceRepository deviceRepo;
    private final ModelMapper mapper;

    // -------------------- Create --------------------
    @Override
    @Transactional
    public ReviewDTO create(ReviewDTO dto) {
        validateRating(dto.getRating());

        Long deviceId = dto.getDeviceId();
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));

        Review r = mapper.map(dto, Review.class);
        r.setId(null);
        r.setDevice(device);
        r.setReviewerName(clean(dto.getReviewerName()));
        r.setContent(clean(dto.getContent()));
        r.setSourceUrl(clean(dto.getSourceUrl()));

        return toDTO(repo.save(r));
    }

    // -------------------- Read --------------------
    @Override
    @Transactional(readOnly = true)
    public ReviewDTO get(Long id) {
        return toDTO(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> listByDevice(Long deviceId) {
        // If you want to validate the device exists, uncomment next line:
        // deviceRepo.findById(deviceId).orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        return repo.findByDevice_IdOrderByCreatedAtDesc(deviceId)
                .stream().map(this::toDTO).toList();
    }

    // -------------------- Update --------------------
    @Override
    @Transactional
    public ReviewDTO update(Long id, ReviewDTO dto) {
        Review r = find(id);

        if (dto.getReviewerName() != null) r.setReviewerName(clean(dto.getReviewerName()));
        if (dto.getContent() != null)       r.setContent(clean(dto.getContent()));
        if (dto.getSourceUrl() != null)     r.setSourceUrl(clean(dto.getSourceUrl()));

        if (dto.getRating() != null) {
            validateRating(dto.getRating());
            r.setRating(dto.getRating());
        }

        // Note: deviceId is not mutable here.
        return toDTO(repo.save(r));
    }

    // -------------------- Delete --------------------
    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
    }

    // -------------------- Helpers --------------------
    private void validateRating(BigDecimal rating) {
        if (rating == null) return;
        if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Rating must be in [0..5]");
        }
    }

    private Review find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + id));
    }

    private String clean(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private ReviewDTO toDTO(Review r) {
        ReviewDTO dto = mapper.map(r, ReviewDTO.class);
        dto.setDeviceId(r.getDevice() != null ? r.getDevice().getId() : null);
        return dto;
    }
}