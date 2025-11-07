package com.bamako.fuelqueue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@ToString(callSuper = true, exclude = {"user", "locality"})
@Entity
@Table(name = "user_localities", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_locality_pair", columnNames = {"user_id", "locality_id"})
})
public class UserLocality extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_id", nullable = false)
    @ToString.Exclude
    private Locality locality;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;
}
