package com.albuquerque.map_market_v1.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "shelving_units")
public class ShelvingUnitEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long unit;

    @Column(name = "side_A", nullable = false, length = 180)
    private String sideA;

    @Column(name = "side_B", length = 180)
    private String sideB;

    public ShelvingUnitEntity() {
    }

    public ShelvingUnitEntity(Long unit, String sideA, String sideB) {
        this.unit = unit;
        this.sideA = sideA;
        this.sideB = sideB;
    }

    public ShelvingUnitEntity(Long id, Long unit, String sideA, String sideB) {
        this.id = id;
        this.unit = unit;
        this.sideA = sideA;
        this.sideB = sideB;
    }

    public Long getId() {
        return id;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }

    public String getSideA() {
        return sideA;
    }

    public void setSideA(String sideA) {
        this.sideA = sideA;
    }

    public String getSideB() {
        return sideB;
    }

    public void setSideB(String sideB) {
        this.sideB = sideB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShelvingUnitEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(unit, that.unit) && Objects.equals(sideA, that.sideA) && Objects.equals(sideB, that.sideB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unit, sideA, sideB);
    }
}
