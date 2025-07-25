// File: com.comparemydevice.backend.entity.Device.java
package com.comparemydevice.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private String brand;
    private String modelNumber;
    private String os;
    private String processor;
    private String graphics;
    private String ram;
    private String storageOptions;
    private String colorVariants;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String warrantyInfo;
    private String returnPolicy;

    private String batteryCapacity;
    private String chargingSpeed;
    private String chargingType;
    private boolean otgSupport;

    private String cameraPrimary;
    private String cameraUltraWide;
    private String cameraMacro;
    private String selfieCamera;
    private String flashType;
    private String zoomLevel;
    private String videoRecording;

    private String displaySize;
    private String displayType;
    private String resolution;
    private String refreshRate;
    private String brightnessNits;
    private String aspectRatio;
    private String screenToBodyRatio;
    private String touchSamplingRate;
    private String displayFeatures;

    private String networkSupport;
    private String simType;
    private String wifi;
    private String bluetooth;
    private String usbType;
    private boolean nfcSupport;

    private String fingerprintSensor;
    private boolean faceUnlock;
    private String sensorList;

    @Min(1000)
    private Integer priceInINR;
    private Integer exchangeOfferPrice;
    private Float ratings;
    private Integer reviewsCount;
    private String imageUrl;
    private String availability;
    private String seller;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String deliveryDate;
}
