package com.bamako.fuelqueue.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "localities")
public class Locality extends AbstractAuditableEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String region;

    @Builder.Default
    @OneToMany(mappedBy = "locality", fetch = FetchType.LAZY)
    private Set<Station> stations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "locality", fetch = FetchType.LAZY)
    private Set<UserLocality> users = new HashSet<>();
}
