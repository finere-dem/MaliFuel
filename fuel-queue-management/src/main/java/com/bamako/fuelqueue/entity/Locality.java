package com.bamako.fuelqueue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
@Entity
@Table(name = "localities", uniqueConstraints = {
        @UniqueConstraint(name = "uk_localities_name_region", columnNames = {"name", "region"})
})
public class Locality extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 120)
    private String region;

    @Builder.Default
    @OneToMany(mappedBy = "locality")
    @ToString.Exclude
    private Set<Station> stations = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "locality")
    @ToString.Exclude
    private Set<UserLocality> userLocalities = new LinkedHashSet<>();
}
