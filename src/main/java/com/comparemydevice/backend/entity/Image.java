package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "image",
        indexes = @Index(name = "idx_image_device_id", columnList = "device_id")
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String url;

    @Column(name = "alt_text", columnDefinition = "text")
    private String altText;

    @Builder.Default
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = Boolean.FALSE;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false, foreignKey = @ForeignKey(name = "fk_image_device"))
    @ToString.Exclude
    private Device device;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}