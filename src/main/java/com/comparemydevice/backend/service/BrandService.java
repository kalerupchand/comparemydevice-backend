package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.BrandDTO;

import java.util.List;

/**
 * Service interface for managing Brand entities in the system.
 * Provides CRUD operations for interacting with brand data.
 */
public interface BrandService {

    /**
     * Creates a new brand in the system.
     *
     * @param brandDTO the brand details to be created
     * @return the created BrandDTO with generated ID and timestamps
     */
    BrandDTO createBrand(BrandDTO brandDTO);

    /**
     * Retrieves a list of all brands available.
     *
     * @return a list of BrandDTOs
     */
    List<BrandDTO> getAllBrands();

    /**
     * Retrieves a brand by its unique identifier.
     *
     * @param id the ID of the brand
     * @return the BrandDTO corresponding to the ID
     */
    BrandDTO getBrandById(Long id);

    /**
     * Updates an existing brand with new information.
     *
     * @param id the ID of the brand to be updated
     * @param brandDTO the updated brand data
     * @return the updated BrandDTO
     */
    BrandDTO updateBrand(Long id, BrandDTO brandDTO);

    /**
     * Deletes a brand from the system.
     *
     * @param id the ID of the brand to be deleted
     */
    void deleteBrand(Long id);
}