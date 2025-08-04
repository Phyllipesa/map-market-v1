package com.albuquerque.map_market_v1.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntityMapper {

    private final ModelMapper modelMapper;

    public EntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <Origin, Destiny> Destiny parseObject(Origin origin, Class<Destiny> destination) {
        return modelMapper.map(origin, destination);
    }

    public <Origin, Destiny> List<Destiny> parseListObject(List<Origin> originList, Class<Destiny> destinationList) {
        List<Destiny> destinationObjects = new ArrayList<>();

        for (Origin origin : originList) {
            destinationObjects.add(parseObject(origin, destinationList));
        }
        return destinationObjects;
    }
}
