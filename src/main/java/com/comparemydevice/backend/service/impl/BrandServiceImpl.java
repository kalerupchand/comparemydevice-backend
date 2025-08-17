package com.comparemydevice.backend.service.impl;

import com.comparemydevice.backend.dto.BrandDTO;
import com.comparemydevice.backend.entity.Brand;
import com.comparemydevice.backend.exception.ResourceNotFoundException;
import com.comparemydevice.backend.repository.BrandRepository;
import com.comparemydevice.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    public BrandDTO createBrand(BrandDTO brandDTO) {
        Brand brand = modelMapper.map(brandDTO, Brand.class);
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        Brand savedBrand = brandRepository.save(brand);
        return modelMapper.map(savedBrand, BrandDTO.class);
    }

    @Override
    public List<BrandDTO> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BrandDTO getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return modelMapper.map(brand, BrandDTO.class);
    }

    @Override
    public BrandDTO updateBrand(Long id, BrandDTO brandDTO) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        brand.setName(brandDTO.getName());
        brand.setLogoUrl(brandDTO.getLogoUrl());
        brand.setUpdatedAt(LocalDateTime.now());

        Brand updatedBrand = brandRepository.save(brand);
        return modelMapper.map(updatedBrand, BrandDTO.class);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        brandRepository.delete(brand);
    }
}