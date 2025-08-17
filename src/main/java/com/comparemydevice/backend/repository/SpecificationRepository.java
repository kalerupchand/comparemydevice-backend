package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    List<Specification> findByDeviceId(Long deviceId);
}