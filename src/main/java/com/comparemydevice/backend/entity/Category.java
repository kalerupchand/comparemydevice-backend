package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
        name = "category",
        indexes = @Index(name = "idx_category_slug", columnList = "slug"),
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_category_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_category_slug", columnNames = "slug")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 150)
    private String slug;

    @Column(name = "icon_url", columnDefinition = "text")
    private String iconUrl;

    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Device> devices = new LinkedHashSet<>();
}