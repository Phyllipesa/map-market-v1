package com.albuquerque.map_market_v1.entities.dtos.output;

public class ShelvingUnitDto {
    private Long id;
    private Long unit;
    private String sideA;
    private String sideB;

    public ShelvingUnitDto() {
    }

    public ShelvingUnitDto(Long id, Long unit, String sideA, String sideB) {
        this.id = id;
        this.unit = unit;
        this.sideA = sideA;
        this.sideB = sideB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
