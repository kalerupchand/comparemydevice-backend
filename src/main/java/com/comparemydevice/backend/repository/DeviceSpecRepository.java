package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.DeviceSpec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceSpecRepository extends JpaRepository<DeviceSpec, Long> {

    Optional<DeviceSpec> findByDevice_IdAndSpecKey_Id(Long deviceId, Long specKeyId);

    boolean existsByDevice_IdAndSpecKey_Id(Long deviceId, Long specKeyId);

    List<DeviceSpec> findByDevice_Id(Long deviceId);
}