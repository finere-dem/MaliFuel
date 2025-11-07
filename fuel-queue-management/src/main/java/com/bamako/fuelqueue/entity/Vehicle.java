package com.bamako.fuelqueue.entity;

import com.bamako.fuelqueue.enums.FuelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(callSuper = true, exclude = {"user"})
@Entity
@Table(name = "vehicles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehicle_plate", columnNames = "plate_number")
})
public class Vehicle extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    private User user;

    @Column(name = "plate_number", nullable = false, unique = true, length = 15)
    private String plateNumber;

    @Column(nullable = false, length = 80)
    private String make;

    @Column(nullable = false, length = 80)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FuelType fuelType;
}
