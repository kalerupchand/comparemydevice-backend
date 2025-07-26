package com.comparemydevice.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Entity
public class Device {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String processor;
    private String ram;
    private String storage;

    // ✅ Default constructor (required by JPA)
    public Device() {
    }

    // ✅ Constructor with arguments (useful for manual object creation)
    public Device(String name, String processor, String ram, String storage) {
        this.name = name;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getProcessor() { return processor; }

    public void setProcessor(String processor) { this.processor = processor; }

    public String getRam() { return ram; }

    public void setRam(String ram) { this.ram = ram; }

    public String getStorage() { return storage; }

    public void setStorage(String storage) { this.storage = storage; }
}