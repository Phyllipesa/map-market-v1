package com.albuquerque.map_market_v1.services;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestPatchShelvingUnitDto;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestShelvingUnitDto;
import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.exception.InternalServerErrorException;
import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.ShelvingUnitErrorMessages;
import com.albuquerque.map_market_v1.logic.ShelvingUnitValidator;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.ShelvingUnitPersistence;
import com.albuquerque.map_market_v1.repositories.interfaces.input.ServicePatchUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.ServiceUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShelvingUnitService implements
    ServiceUseCase<RequestShelvingUnitDto, ShelvingUnitDto>,
    ServicePatchUseCase<RequestPatchShelvingUnitDto, ShelvingUnitDto> {

    private final ShelvingUnitPersistence persistence;
    private final ShelvingUnitValidator validator;
    private final EntityMapper mapper;

    public ShelvingUnitService(ShelvingUnitPersistence persistence, ShelvingUnitValidator validator, EntityMapper mapper) {
        this.persistence = persistence;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public List<ShelvingUnitDto> findAll() {
        List<ShelvingUnitEntity> shelvingUnits = persistence.findAll();
        if (shelvingUnits.isEmpty())
            throw new ResourceNotFoundException(ShelvingUnitErrorMessages.SHELVING_UNITS_NOT_FOUND);
        return mapper.parseListObject(shelvingUnits, ShelvingUnitDto.class);
    }

    @Override
    public ShelvingUnitDto findById(Long id) {
        ShelvingUnitEntity entity = persistence.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ShelvingUnitErrorMessages.SHELVING_NOT_FOUND + id));
        return mapper.parseObject(entity, ShelvingUnitDto.class);
    }

    @Override
    public ShelvingUnitDto create(RequestShelvingUnitDto data) {
        validator.validate(data);
        ShelvingUnitEntity newShelvingUnit = mapper.parseObject(data, ShelvingUnitEntity.class);
        ShelvingUnitEntity fromEntity = persistence.create(newShelvingUnit)
            .orElseThrow(() -> new InternalServerErrorException(ShelvingUnitErrorMessages.ERROR_CREATING_SHELVING_UNIT));
        return mapper.parseObject(fromEntity, ShelvingUnitDto.class);
    }

    @Transactional
    @Override
    public ShelvingUnitDto update(Long id, RequestShelvingUnitDto data) {
        validator.validate(data);
        ShelvingUnitEntity entity = persistence.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ShelvingUnitErrorMessages.SHELVING_NOT_FOUND + id));

        updateData(entity, data);

        ShelvingUnitEntity updatedEntity = persistence.update(entity)
            .orElseThrow(() -> new InternalServerErrorException(ShelvingUnitErrorMessages.INTERNAL_SERVER_ERROR));

        return mapper.parseObject(updatedEntity, ShelvingUnitDto.class);
    }

    @Transactional
    @Override
    public ShelvingUnitDto patch(Long id, RequestPatchShelvingUnitDto data) {
        ShelvingUnitEntity entity = persistence.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ShelvingUnitErrorMessages.SHELVING_NOT_FOUND + id));

        entity.setSideB(data.getSideB());

        ShelvingUnitEntity updatedEntity = persistence.update(entity)
            .orElseThrow(() -> new InternalServerErrorException(ShelvingUnitErrorMessages.INTERNAL_SERVER_ERROR));

        return mapper.parseObject(updatedEntity, ShelvingUnitDto.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        existResource(id);
        persistence.delete(id);
    }

    private void existResource(Long id) {
        if (!persistence.existResource(id))
            throw new ResourceNotFoundException(ShelvingUnitErrorMessages.SHELVING_NOT_FOUND + id);
    }

    private void updateData(ShelvingUnitEntity entity, RequestShelvingUnitDto data) {
        entity.setUnit(data.getUnit());
        entity.setSideA(data.getSideA());
        entity.setSideB(data.getSideB());
    }
}
