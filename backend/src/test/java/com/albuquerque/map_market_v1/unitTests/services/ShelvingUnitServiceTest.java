package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestPatchShelvingUnitDto;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestShelvingUnitDto;
import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.logic.ShelvingUnitValidator;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.ShelvingUnitPersistence;
import com.albuquerque.map_market_v1.services.ShelvingUnitService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.albuquerque.map_market_v1.unitTests.mocks.ShelvingUnitMockFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelvingUnitServiceTest {

    @InjectMocks
    private ShelvingUnitService service;

    @Mock
    private ShelvingUnitPersistence persistence;

    @Mock
    private ShelvingUnitValidator validator;

    @Mock
    private EntityMapper mapper;

    @BeforeEach
    void setup() {
        service = new ShelvingUnitService(persistence, validator, mapper);
    }

    @Test
    void shouldReturnListOfAllShelvingUnits() {
        // given
        List<ShelvingUnitEntity> entityList = mockResponseShelvingUnitEntityList();
        List<ShelvingUnitDto> dtoList = mockResponseShelvingUnitDtoList();

        when(persistence.findAll()).thenReturn(entityList);
        when(mapper.parseListObject(entityList, ShelvingUnitDto.class)).thenReturn(dtoList);

        // when
        List<ShelvingUnitDto> res = service.findAll();

        // then
        assertEquals(2, res.size());

        ShelvingUnitDto first = res.get(0);
        assertAll("First shelving unit",
            () -> assertEquals(1L, first.getId()),
            () -> assertEquals(1L, first.getUnit()),
            () -> assertEquals("Grãos", first.getSideA()),
            () -> assertEquals("Farinhas", first.getSideB())
        );

        ShelvingUnitDto last = res.get(1);
        assertAll("Second shelving unit",
            () -> assertEquals(2L, last.getId()),
            () -> assertEquals(2L, last.getUnit()),
            () -> assertEquals("Bolachas e Biscoitos", last.getSideA()),
            () -> assertEquals("Sobremesas e Doces", last.getSideB())
        );
    }

    @Test
    void shouldReturnShelvingUnitWhenIdExists() {
        // given
        Long id = 1L;
        ShelvingUnitEntity fromEntity = mockShelvingUnitFromEntity();
        ShelvingUnitDto response = mockResponseShelvingUnitDto();

        when(persistence.findById(id)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, ShelvingUnitDto.class)).thenReturn(response);

        // when
        ShelvingUnitDto result = service.findById(id);

        // then
        assertAll("ShelvingUnit by ID",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals(1L, result.getUnit()),
            () -> assertEquals("Grãos", result.getSideA()),
            () -> assertEquals("Farinhas", result.getSideB())
        );
    }

    @Test
    void shouldCreateShelvingUnitSuccessfully() {
        // given
        RequestShelvingUnitDto request = mockRequestShelvingUnitDto();
        ShelvingUnitEntity toEntity = mockShelvingUnitToEntity();
        ShelvingUnitEntity fromEntity = mockShelvingUnitFromEntity();
        ShelvingUnitDto response = mockResponseShelvingUnitDto();

        // when
        doNothing().when(validator).validate(request);
        when(mapper.parseObject(request, ShelvingUnitEntity.class)).thenReturn(toEntity);
        when(persistence.create(toEntity)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, ShelvingUnitDto.class)).thenReturn(response);
        ShelvingUnitDto result = service.create(request);

        // then
        verify(validator).validate(request);
        verify(mapper).parseObject(request, ShelvingUnitEntity.class);
        verify(persistence).create(toEntity);
        verify(mapper).parseObject(fromEntity, ShelvingUnitDto.class);

        assertAll("Create",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals(1L, result.getUnit()),
            () -> assertEquals("Grãos", result.getSideA()),
            () -> assertEquals("Farinhas", result.getSideB())
        );
    }

    @Test
    void shouldUpdateShelvingUnitSuccessfully() {
        // given
        Long id = 1L;
        RequestShelvingUnitDto request = mockRequestShelvingUnitDto();
        ShelvingUnitEntity toUpdate = mockShelvingUnitToEntity();
        ShelvingUnitEntity fromEntity = mockShelvingUnitFromEntity();
        ShelvingUnitDto response = mockResponseShelvingUnitDto();

        // when
        doNothing().when(validator).validate(request);
        when(persistence.findById(id)).thenReturn(Optional.of(toUpdate));
        when(persistence.update(toUpdate)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, ShelvingUnitDto.class)).thenReturn(response);

        ShelvingUnitDto result = service.update(1L, request);

        // then
        verify(validator).validate(request);
        verify(persistence).update(toUpdate);
        verify(mapper).parseObject(fromEntity, ShelvingUnitDto.class);

        assertAll("Update",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals(1L, result.getUnit()),
            () -> assertEquals("Grãos", result.getSideA()),
            () -> assertEquals("Farinhas", result.getSideB())
        );
    }

    @Test
    void shouldUpdateShelvingUnitPartiallySuccessfully() {
        // given
        Long id = 1L;
        RequestPatchShelvingUnitDto request = mockRequestPatchShelvingUnitDto();
        ShelvingUnitEntity entity = new ShelvingUnitEntity(1L, 1L, "Grãos", null);
        ShelvingUnitEntity fromEntity = mockShelvingUnitFromEntity();
        ShelvingUnitDto response = mockResponseShelvingUnitDto();

        // when
        when(persistence.findById(id)).thenReturn(Optional.of(entity));
        entity.setSideB(request.getSideB());
        when(persistence.update(entity)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, ShelvingUnitDto.class)).thenReturn(response);

        ShelvingUnitDto result = service.patch(id, request);

        // then
        verify(persistence).findById(id);
        verify(persistence).update(entity);
        verify(mapper).parseObject(fromEntity, ShelvingUnitDto.class);

        assertAll("Patch",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals(1L, result.getUnit()),
            () -> assertEquals("Grãos", result.getSideA()),
            () -> assertEquals("Farinhas", result.getSideB())
        );
    }

    @Test
    void shouldDeleteShelvingUnitSuccessfully() {
        // given
        Long id = 1L;

        // when
        when(persistence.existResource(1L)).thenReturn(true);
        service.delete(id);

        // then
        verify(persistence, times(1)).existResource(id);
        verify(persistence, times(1)).delete(id);
    }
}
