package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "specification",
        uniqueConstraints = @UniqueConstraint(name = "uk_spec_device_key", columnNames = {"device_id", "key"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Note: "key" is allowed in Postgres; if you ever migrate, consider "spec_key"
    @Column(name = "key", nullable = false)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    @Column(name = "spec_type")
    private String specType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_spec_device"))
    private Device device;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}