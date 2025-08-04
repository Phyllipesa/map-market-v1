package com.albuquerque.map_market_v1.logic;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestShelvingUnitDto;
import com.albuquerque.map_market_v1.exception.InvalidShelvingException;
import com.albuquerque.map_market_v1.exception.messages.ShelvingUnitErrorMessages;
import org.springframework.stereotype.Component;

@Component
public class ShelvingUnitValidator {
    public void validate(RequestShelvingUnitDto request) {
        validateUnit(request.getUnit());
        validateUnitNotNegative(request.getUnit());
        validateNotNullSideA(request.getSideA());
    }

    public void validateUnit(Long unit) {
        if (unit == null)
            throw new InvalidShelvingException(ShelvingUnitErrorMessages.requiredParameterMessage("unit"));
    }

    public void validateUnitNotNegative(Long unit) {
        if (unit <= 0)
            throw new InvalidShelvingException(ShelvingUnitErrorMessages.NEGATIVE_NOT_ALLOWED);
    }

    public void validateNotNullSideA(String sideA) {
        if (sideA == null || sideA.isEmpty())
            throw new InvalidShelvingException(ShelvingUnitErrorMessages.requiredParameterMessage("sideA"));
    }
}
