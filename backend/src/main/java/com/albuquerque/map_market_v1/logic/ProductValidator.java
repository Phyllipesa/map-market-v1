package com.albuquerque.map_market_v1.logic;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestProductDto;
import com.albuquerque.map_market_v1.exception.InvalidProductException;
import com.albuquerque.map_market_v1.exception.messages.ProductErrorMessages;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductValidator {
    public void validate(RequestProductDto productRequestDto) {
        validateName(productRequestDto.getName());
        validatePriceNotNull(productRequestDto.getPrice());
        validatePriceNotNegative(productRequestDto.getPrice());
    }

    public void validateName(String nome) {
        if (nome == null || nome.isEmpty() || nome.isBlank())
            throw new InvalidProductException(ProductErrorMessages.requiredParameterMessage("name"));
    }

    public void validatePriceNotNegative(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidProductException(ProductErrorMessages.NEGATIVE_NOT_ALLOWED);
    }

    public void validatePriceNotNull(BigDecimal price) {
        if (price == null)
            throw new InvalidProductException(ProductErrorMessages.requiredParameterMessage("price"));
    }
}
