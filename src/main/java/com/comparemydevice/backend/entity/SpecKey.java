package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "spec_key")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpecKey {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "spec_type", length = 50)
    private String specType;

    @OneToMany(mappedBy = "specKey")
    private Set<DeviceSpec> deviceSpecs;
}