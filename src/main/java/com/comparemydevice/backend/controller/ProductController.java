package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.ProductDetailsDTO;
import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.DeviceRepository;
import com.comparemydevice.backend.service.mapper.ProductDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final DeviceRepository deviceRepo;
    private final ProductDetailsMapper mapper;

    @GetMapping("/{id}/details")
    public ProductDetailsDTO getDetails(@PathVariable Long id) {
        Device d = deviceRepo.findWithRelationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));
        return mapper.toDetails(d);
    }
}