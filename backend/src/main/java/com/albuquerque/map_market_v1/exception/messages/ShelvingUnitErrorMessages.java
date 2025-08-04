package com.albuquerque.map_market_v1.exception.messages;

public class ShelvingUnitErrorMessages {
    public static final String ERROR_CREATING_SHELVING_UNIT = "Error occurred while creating the Shelving Unit";
    public static final String SHELVING_UNITS_NOT_FOUND = "Shelving units not found!";
    public static final String SHELVING_NOT_FOUND = "Shelving not found with id ";

    public static final String NEGATIVE_NOT_ALLOWED = "Negative numbers are not allowed";
    public static final String INTERNAL_SERVER_ERROR = "Erro Interno do Servidor";

    public static String requiredParameterMessage(String parameterName) {
        return "Required parameter '" + parameterName + "' is null or missing";
    }
}
