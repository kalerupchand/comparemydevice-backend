package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findBySlug(String slug);
    boolean existsBySlug(String slug);
}