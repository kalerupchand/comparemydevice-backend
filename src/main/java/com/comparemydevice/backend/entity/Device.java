package com.comparemydevice.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Device {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String processor;
    private String ram;
    private String storage;

    public Device() {}

    public Device(String name, String processor, String ram, String storage) {
        this.name = name;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }

    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }

    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }
}