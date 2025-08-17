package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String processor;
    private String ram;
    private String storage;

    @Column(name = "price_ininr", precision = 10, scale = 2)
    private BigDecimal priceInINR;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String slug;

    // Postgres text[] support (Hibernate 6)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // ✅ Map to existing DB columns "brand" and "category"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_device_brand"))
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_device_category"))
    private Category category;

    // Back-references so you can load with EntityGraphs like "images/specifications/reviews"
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specification> specifications;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}