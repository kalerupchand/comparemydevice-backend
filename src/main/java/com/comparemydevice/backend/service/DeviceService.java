package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;

import java.util.List;

/**
 * Service interface for managing Device entities.
 * Provides operations to create, retrieve, update, and delete devices.
 */
public interface DeviceService {

    /**
     * Creates a new device entry in the system.
     *
     * @param deviceDTO the device details to create
     * @return the created DeviceDTO with generated ID and timestamps
     */
    DeviceDTO createDevice(DeviceDTO deviceDTO);

    /**
     * Retrieves a list of all devices in the system.
     *
     * @return a list of DeviceDTOs
     */
    List<DeviceDTO> getAllDevices();

    /**
     * Retrieves a device by its unique ID.
     *
     * @param id the ID of the device to retrieve
     * @return the corresponding DeviceDTO
     */
    DeviceDTO getDeviceById(Long id);

    /**
     * Updates an existing device's details.
     *
     * @param id the ID of the device to update
     * @param deviceDTO the updated device data
     * @return the updated DeviceDTO
     */
    DeviceDTO updateDevice(Long id, DeviceDTO deviceDTO);

    /**
     * Deletes a device by its ID.
     *
     * @param id the ID of the device to delete
     */
    void deleteDevice(Long id);
}