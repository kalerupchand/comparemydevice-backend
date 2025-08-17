package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.SpecificationDTO;

import java.util.List;

/**
 * Service interface for managing specifications of a device.
 * <p>
 * This interface defines operations for creating, updating, retrieving, and deleting specifications
 * which are typically grouped by category (e.g., Display, Battery, Camera) and associated with a specific device.
 */
public interface SpecificationService {

    /**
     * Creates and stores a new specification for the specified device.
     *
     * @param deviceId         the ID of the device to associate the specification with
     * @param specificationDTO the specification details to be saved
     * @return the created SpecificationDTO
     */
    SpecificationDTO createSpecification(Long deviceId, SpecificationDTO specificationDTO);

    /**
     * Retrieves all specifications associated with a specific device.
     *
     * @param deviceId the ID of the device
     * @return a list of SpecificationDTOs
     */
    List<SpecificationDTO> getSpecificationsByDeviceId(Long deviceId);

    /**
     * Updates an existing specification by its ID.
     *
     * @param specId           the ID of the specification to update
     * @param specificationDTO the updated specification details
     * @return the updated SpecificationDTO
     */
    SpecificationDTO updateSpecification(Long specId, SpecificationDTO specificationDTO);

    /**
     * Deletes a specification by its ID.
     *
     * @param specId the ID of the specification to delete
     */
    void deleteSpecification(Long specId);
}