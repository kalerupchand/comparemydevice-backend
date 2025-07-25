package com.comparemydevice.backend;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private static final List<Device> devices = List.of(
        new Device("Samsung Galaxy S24", "Snapdragon 8 Gen 3", "8GB", "128GB"),
        new Device("iPhone 15", "A17 Bionic", "6GB", "128GB"),
        new Device("OnePlus 12", "Snapdragon 8 Gen 3", "12GB", "256GB")
    );

    @GetMapping
    public List<Device> getAllDevices() {
        return devices;
    }

    @GetMapping("/compare")
    public List<Device> compareDevices(@RequestParam List<String> names) {
        List<Device> result = new ArrayList<>();
        for (Device d : devices) {
            if (names.contains(d.name())) {
                result.add(d);
            }
        }
        return result;
    }
}