package com.comparemydevice.backend.service;

import com.comparemydevice.backend.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for managing image resources related to devices.
 * Supports uploading via file or URL, retrieving, and deleting images.
 */
public interface ImageService {

    /**
     * Uploads an image file and associates it with a specific device.
     *
     * @param deviceId the ID of the device to associate the image with
     * @param file the image file to upload
     * @param altText optional alternate text for the image
     * @return the uploaded ImageDTO
     * @throws IOException if there is an error reading the file
     */
    ImageDTO uploadImage(Long deviceId, MultipartFile file, String altText) throws IOException;

    /**
     * Associates an image URL (typically from CSV) with a device.
     *
     * @param deviceId the ID of the device
     * @param url the image URL
     * @param altText optional alternate text
     * @param isPrimary whether the image is primary
     * @return the created ImageDTO
     */
    ImageDTO addImageFromUrl(Long deviceId, String url, String altText, boolean isPrimary);

    /**
     * Retrieves all images associated with a specific device.
     *
     * @param deviceId the ID of the device
     * @return a list of ImageDTOs
     */
    List<ImageDTO> getImagesByDeviceId(Long deviceId);

    /**
     * Deletes an image by its ID.
     *
     * @param imageId the ID of the image to delete
     */
    void deleteImage(Long imageId);
}