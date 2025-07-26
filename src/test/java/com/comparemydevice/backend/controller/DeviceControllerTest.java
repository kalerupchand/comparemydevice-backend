// File: com.comparemydevice.backend.controller.DeviceControllerTest.java
package com.comparemydevice.backend.controller;

import com.comparemydevice.backend.entity.Device;
import com.comparemydevice.backend.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    private Device testDevice;

    @BeforeEach
    void setUp() {
        testDevice = Device.builder()
                .id(1L)
                .name("Test Phone")
                .brand("TestBrand")
                .priceInINR(10000)
                .build();
    }

    @Test
    void shouldReturnListOfDevices() throws Exception {
        Mockito.when(deviceService.getAllDevices()).thenReturn(List.of(testDevice));

        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Phone")));
    }

    @Test
    void shouldReturnDeviceById() throws Exception {
        Mockito.when(deviceService.getDeviceById(1L)).thenReturn(Optional.of(testDevice));

        mockMvc.perform(get("/api/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Phone")));
    }

    @Test
    void shouldAddDevice() throws Exception {
        Mockito.when(deviceService.addDevice(Mockito.any(Device.class))).thenReturn(testDevice);

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testDevice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Phone")));
    }

    @Test
    void shouldDeleteDevice() throws Exception {
        Mockito.doNothing().when(deviceService).deleteDevice(1L);

        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isOk());
    }
}