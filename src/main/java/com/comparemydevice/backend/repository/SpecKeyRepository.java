// src/main/java/com/comparemydevice/backend/repository/SpecKeyRepository.java
package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.SpecKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecKeyRepository extends JpaRepository<SpecKey, Long> {
    boolean existsByName(String name); // ✅ use name, not key
}