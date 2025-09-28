package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "device",
        indexes = {
                @Index(name = "idx_device_brand_id", columnList = "brand_id"),
                @Index(name = "idx_device_category_id", columnList = "category_id"),
                @Index(name = "idx_device_release_date", columnList = "release_date"),
                @Index(name = "idx_device_price", columnList = "price_amount"),
                @Index(name = "idx_device_is_deleted", columnList = "is_deleted")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_device_slug", columnNames = "slug"),
                @UniqueConstraint(name = "uq_device_brand_name_release", columnNames = {"brand_id", "name", "release_date"})
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

    @Column(name = "price_amount", precision = 12, scale = 2)
    private BigDecimal priceAmount;

    @Builder.Default
    @Column(name = "price_currency", length = 3)
    private String priceCurrency = "INR";

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(nullable = false, length = 255)
    private String slug;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    // ---- Relations ----

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_device_brand"))
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_device_category"))
    private Category category;

    /** Use Set to avoid bag issues; order is not enforced at DB level for ManyToMany */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "device_tag",
            joinColumns = @JoinColumn(name = "device_id", foreignKey = @ForeignKey(name = "fk_device_tag_device")),
            inverseJoinColumns = @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_device_tag_tag"))
    )
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Tag> tags = new LinkedHashSet<>();

    /** Keep exactly one bag(List) to avoid MultipleBagFetchException */
    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isPrimary DESC, sortOrder ASC, id ASC")
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    /** Use Set to avoid multiple bags */
    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC")
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Review> reviews = new LinkedHashSet<>();

    /** Use Set to avoid multiple bags; order only by local field */
    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<DeviceSpec> deviceSpecs = new LinkedHashSet<>();

    // ---- Timestamps ----

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}