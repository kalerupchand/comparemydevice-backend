package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.dto.BrandDTO;
import com.comparemydevice.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing brands.
 */
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /**
     * Creates a new brand.
     *
     * @param brandDTO the brand data to create
     * @return the created BrandDTO
     */
    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        return ResponseEntity.ok(brandService.createBrand(brandDTO));
    }

    /**
     * Retrieves all brands.
     *
     * @return list of all brands
     */
    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    /**
     * Retrieves a specific brand by ID.
     *
     * @param id the ID of the brand
     * @return the corresponding BrandDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    /**
     * Updates a brand by ID.
     *
     * @param id the ID of the brand to update
     * @param brandDTO updated brand details
     * @return updated BrandDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Long id, @RequestBody BrandDTO brandDTO) {
        return ResponseEntity.ok(brandService.updateBrand(id, brandDTO));
    }

    /**
     * Deletes a brand by ID.
     *
     * @param id the ID of the brand to delete
     * @return HTTP 204 on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}