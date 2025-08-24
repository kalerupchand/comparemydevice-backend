package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "device_spec",
        uniqueConstraints = @UniqueConstraint(name = "uq_device_spec", columnNames = {"device_id", "spec_key_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceSpec {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_device_spec_device"))
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spec_key_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_device_spec_speckey"))
    private SpecKey specKey;

    @Lob
    @Column(name = "value_text")
    private String valueText;

    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}