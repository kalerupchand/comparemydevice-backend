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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDTO createReview(Long deviceId, ReviewDTO reviewDTO) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + deviceId));

        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setDevice(device);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return modelMapper.map(reviewRepository.save(review), ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> getReviewsByDeviceId(Long deviceId) {
        List<Review> reviews = reviewRepository.findByDeviceId(deviceId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        review.setReviewerName(reviewDTO.getReviewerName());
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        review.setSource(reviewDTO.getSource());
        review.setUpdatedAt(LocalDateTime.now());

        return modelMapper.map(reviewRepository.save(review), ReviewDTO.class);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }
}