package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "device",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_device_brand_name_release",
                columnNames = {"brand_id","name","release_date"}
        ),
        indexes = {
                @Index(name = "idx_device_brand_id", columnList = "brand_id"),
                @Index(name = "idx_device_category_id", columnList = "category_id"),
                @Index(name = "idx_device_release_date", columnList = "release_date"),
                @Index(name = "idx_device_price", columnList = "price_amount"),
                @Index(name = "idx_device_is_deleted", columnList = "is_deleted"),
                @Index(name = "idx_device_slug", columnList = "slug")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Device {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    private String processor;
    private String ram;
    private String storage;

    // matches Flyway: price_amount NUMERIC(12,2), price_currency CHAR(3)
    @Column(name = "price_amount", precision = 12, scale = 2)
    private BigDecimal priceAmount;

    @Column(name = "price_currency", length = 3)
    @Builder.Default
    private String priceCurrency = "INR";

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;

    // --- parents ---
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_device_brand_id"))
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_device_category_id"))
    private Category category;

    // --- tags (join table device_tag) ---
    @ManyToMany
    @JoinTable(
            name = "device_tag",
            joinColumns = @JoinColumn(name = "device_id",
                    foreignKey = @ForeignKey(name = "fk_device_tag_device")),
            inverseJoinColumns = @JoinColumn(name = "tag_id",
                    foreignKey = @ForeignKey(name = "fk_device_tag_tag"))
    )
    @Builder.Default
    private Set<Tag> tags = new LinkedHashSet<>();

    // --- child collections (match other entities' mappedBy="device") ---
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isPrimary DESC, sortOrder ASC, id ASC")
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private Set<DeviceSpec> deviceSpecs = new LinkedHashSet<>();

    // timestamps (DB default/trigger or JPA @Creation/@Update)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}