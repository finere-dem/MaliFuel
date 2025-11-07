package com.bamako.fuelqueue.domain.entity;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.bamako.fuelqueue.util.FuelCapacityConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stations")
public class Station extends AbstractAuditableEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "locality_id", nullable = false)
    private Locality locality;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private boolean ravitailed;

    @Builder.Default
    @Convert(converter = FuelCapacityConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<FuelType, Integer> maxCapacityPerFuel = new EnumMap<>(FuelType.class);

    @Builder.Default
    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY)
    private Set<ConsumptionRecord> consumptionRecords = new HashSet<>();
}
