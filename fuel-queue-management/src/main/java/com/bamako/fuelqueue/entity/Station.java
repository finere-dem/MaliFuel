package com.bamako.fuelqueue.entity;

import com.bamako.fuelqueue.enums.FuelType;
import com.bamako.fuelqueue.util.FuelCapacityConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true)
@Entity
@Table(name = "stations", uniqueConstraints = {
        @UniqueConstraint(name = "uk_station_name_locality", columnNames = {"name", "locality_id"})
})
public class Station extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_id", nullable = false)
    @ToString.Exclude
    private Locality locality;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private boolean ravitailed;

    @Convert(converter = FuelCapacityConverter.class)
    @Column(name = "max_capacity_per_fuel", columnDefinition = "jsonb")
    @Builder.Default
    private Map<FuelType, Integer> maxCapacityPerFuel = new LinkedHashMap<>();

    @Builder.Default
    @OneToMany(mappedBy = "station")
    @ToString.Exclude
    private Set<Ticket> tickets = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "station")
    @ToString.Exclude
    private Set<ConsumptionRecord> consumptionRecords = new LinkedHashSet<>();
}
