package com.albuquerque.map_market_v1.repositories.config;

import com.albuquerque.map_market_v1.mapper.EntityMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public EntityMapper entityMapper() {
        return new EntityMapper(modelMapper());
    }
}
