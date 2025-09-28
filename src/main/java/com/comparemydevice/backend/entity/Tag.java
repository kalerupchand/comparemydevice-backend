package com.comparemydevice.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
        name = "tag",
        indexes = @Index(name = "idx_tag_slug", columnList = "slug"),
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tag_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_tag_slug", columnNames = "slug")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String slug;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Device> devices = new LinkedHashSet<>();
}