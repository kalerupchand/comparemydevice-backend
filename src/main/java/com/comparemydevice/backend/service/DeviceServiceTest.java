// File: com.comparemydevice.backend.service.DeviceServiceTest.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.repository.DeviceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private Device sampleDevice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDevice = Device.builder()
                .id(1L)
                .name("iPhone 16 Pro")
                .brand("Apple")
                .priceInINR(142900)
                .build();
    }

    @Test
    void testGetAllDevices() {
        Mockito.when(deviceRepository.findAll()).thenReturn(Arrays.asList(sampleDevice));

        List<Device> result = deviceService.getAllDevices();

        Assertions.assertEquals(1, result.size());
        assertEquals("iPhone 16 Pro", result.get(0).getName());
    }

    @Test
    void testAddDevice() {
        Mockito.when(deviceRepository.save(sampleDevice)).thenReturn(sampleDevice);

        Device result = deviceService.addDevice(sampleDevice);

        Assertions.assertNotNull(result);
        assertEquals("Apple", result.getBrand());
    }

    @Test
    void testGetDeviceById() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));

        Optional<Device> result = deviceService.getDeviceById(1L);

        Assertions.assertTrue(result.isPresent());
        assertEquals("iPhone 16 Pro", result.get().getName());
    }

    @Test
    void testDeleteDevice() {
        deviceService.deleteDevice(1L);
        Mockito.verify(deviceRepository, Mockito.times(1)).deleteById(1L);
    }
}