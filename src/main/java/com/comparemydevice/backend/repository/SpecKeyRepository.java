package com.comparemydevice.backend.repository;

import com.comparemydevice.backend.entity.SpecKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecKeyRepository extends JpaRepository<SpecKey, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<SpecKey> findByNameIgnoreCase(String name);

    List<SpecKey> findAllByOrderByNameAsc();

    List<SpecKey> findBySpecTypeIgnoreCase(String specType);
}