package com.albuquerque.map_market_v1.controllers.dto.input;

public class RequestShelvingUnitDto {
    Long unit;
    String sideA;
    String sideB;

    public RequestShelvingUnitDto() {
    }

    public RequestShelvingUnitDto(Long unit, String sideA, String sideB) {
        this.unit = unit;
        this.sideA = sideA;
        this.sideB = sideB;
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
