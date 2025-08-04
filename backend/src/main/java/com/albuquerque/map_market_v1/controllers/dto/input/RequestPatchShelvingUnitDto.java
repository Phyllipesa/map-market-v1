package com.albuquerque.map_market_v1.controllers.dto.input;

public class RequestPatchShelvingUnitDto {
    String sideB;

    public RequestPatchShelvingUnitDto(String sideB) {
        this.sideB = sideB;
    }

    public String getSideB() {
        return sideB;
    }

    public void setSideB(String sideB) {
        this.sideB = sideB;
    }
}
