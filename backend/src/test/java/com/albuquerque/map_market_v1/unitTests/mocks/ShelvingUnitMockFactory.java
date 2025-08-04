package com.albuquerque.map_market_v1.unitTests.mocks;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestPatchShelvingUnitDto;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestShelvingUnitDto;
import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;

import java.util.List;

public class ShelvingUnitMockFactory {

    public static List<ShelvingUnitEntity> mockResponseShelvingUnitEntityList() {
        return List.of(
            new ShelvingUnitEntity(1L, 1L, "Grãos", "Farinhas"),
            new ShelvingUnitEntity(2L, 2L, "Bolachas e Biscoitos", "Sobremesas e Doces")
        );
    }

    public static List<ShelvingUnitDto> mockResponseShelvingUnitDtoList() {
        return List.of(
            new ShelvingUnitDto(1L, 1L, "Grãos", "Farinhas"),
            new ShelvingUnitDto(2L, 2L, "Bolachas e Biscoitos", "Sobremesas e Doces")
        );
    }

    public static RequestShelvingUnitDto mockRequestShelvingUnitDto() {
        return new RequestShelvingUnitDto(1L, "Grãos", "Farinhas");
    }

    public static RequestPatchShelvingUnitDto mockRequestPatchShelvingUnitDto() {
        return new RequestPatchShelvingUnitDto("Farinhas");
    }

    public static ShelvingUnitDto mockResponseShelvingUnitDto() {
        return new ShelvingUnitDto(1L, 1L, "Grãos", "Farinhas");
    }

    public static ShelvingUnitEntity mockShelvingUnitToEntity() {
        return new ShelvingUnitEntity(1L, "Grãos", "Farinhas");
    }

    public static ShelvingUnitEntity mockShelvingUnitFromEntity() {
        return new ShelvingUnitEntity(1L, 1L, "Grãos", "Farinhas");
    }
}