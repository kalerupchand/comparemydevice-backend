package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.DeviceDTO;

import java.util.List;

/**
 * Service interface for comparing devices.
 * Provides methods to compare two or more devices based on their specifications, features, and reviews.
 */
public interface ComparisonService {

    /**
     * Compares two devices by their IDs and returns a detailed comparison.
     *
     * @param deviceId1 the ID of the first device
     * @param deviceId2 the ID of the second device
     * @return a list containing the two DeviceDTOs with their details, specifications, and comparisons
     */
    List<DeviceDTO> compareDevices(Long deviceId1, Long deviceId2);

    /**
     * Compares a list of device IDs and returns all their details for side-by-side comparison.
     *
     * @param deviceIds the list of device IDs to compare
     * @return a list of DeviceDTOs with all relevant information
     */
    List<DeviceDTO> compareMultipleDevices(List<Long> deviceIds);
}