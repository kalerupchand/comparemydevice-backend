package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
        name = "spec_key",
        uniqueConstraints = @UniqueConstraint(name = "uk_spec_key_name", columnNames = "name")
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpecKey {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "spec_type", length = 50)
    private String specType;

    @OneToMany(mappedBy = "specKey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<DeviceSpec> deviceSpecs = new LinkedHashSet<>();
}