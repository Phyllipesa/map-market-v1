package com.albuquerque.map_market_v1.services;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestProductDto;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.exception.InternalServerErrorException;
import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.ProductErrorMessages;
import com.albuquerque.map_market_v1.logic.ProductValidator;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.ProductPersistence;
import com.albuquerque.map_market_v1.repositories.interfaces.input.ServiceUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements
    ServiceUseCase<RequestProductDto, ProductDto> {

    private final ProductPersistence persistence;
    private final ProductValidator validator;
    private final EntityMapper mapper;

    public ProductService(ProductPersistence persistence, ProductValidator validator, EntityMapper mapper) {
        this.persistence = persistence;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public List<ProductDto> findAll() {
        List<ProductEntity> allProducts = persistence.findAll();
        if (allProducts.isEmpty())
            throw new ResourceNotFoundException(ProductErrorMessages.PRODUCTS_NOT_FOUND);
        return mapper.parseListObject(allProducts, ProductDto.class);
    }

    @Override
    public ProductDto findById(Long id) {
        ProductEntity product = persistence.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + id));
        return mapper.parseObject(product, ProductDto.class);
    }

    @Override
    public ProductDto create(RequestProductDto newProduct) {
        validator.validate(newProduct);
        ProductEntity productEntity = mapper.parseObject(newProduct, ProductEntity.class);
        ProductEntity createdProduct = persistence.create(productEntity)
            .orElseThrow(() -> new InternalServerErrorException (ProductErrorMessages.ERROR_CREATING_PRODUCT));
        return mapper.parseObject(createdProduct, ProductDto.class);
    }

    @Transactional
    @Override
    public ProductDto update(Long id, RequestProductDto data) {
        validator.validate(data);
        ProductEntity entity = persistence.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + id));

        updateData(entity, data);

        entity = persistence.update(entity)
            .orElseThrow(() -> new ResourceNotFoundException(ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + id));

        return mapper.parseObject(entity, ProductDto.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        existResource(id);
        persistence.delete(id);
    }

    private void existResource(Long id) {
        if (!persistence.existResource(id))
            throw new ResourceNotFoundException(ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + id);
    }

    private void updateData(ProductEntity entity, RequestProductDto data) {
        entity.setName(data.getName());
        entity.setPrice(data.getPrice());
    }
}
