// File: com.comparemydevice.backend.service.DeviceServiceTest.java
package com.comparemydevice.backend.service;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeviceServiceTest {

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
                .name("Motorola G85")
                .priceInINR(16999)
                .build();
    }

    @Test
    void testAddDevice() {
        when(deviceRepository.save(sampleDevice)).thenReturn(sampleDevice);
        Device saved = deviceService.addDevice(sampleDevice);
        assertEquals("Motorola G85", saved.getName());
    }

    @Test
    void testGetAllDevices() {
        when(deviceRepository.findAll()).thenReturn(Arrays.asList(sampleDevice));
        List<Device> devices = deviceService.getAllDevices();
        assertEquals(1, devices.size());
    }

    @Test
    void testGetDeviceById() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));
        Optional<Device> result = deviceService.getDeviceById(1L);
        assertTrue(result.isPresent());
        assertEquals("Motorola G85", result.get().getName());
    }

    @Test
    void testDeleteDevice() {
        doNothing().when(deviceRepository).deleteById(1L);
        deviceService.deleteDevice(1L);
        verify(deviceRepository, times(1)).deleteById(1L);
    }
}