package com.albuquerque.map_market_v1.exception.messages;

public class ProductErrorMessages {
    public static final String ERROR_CREATING_PRODUCT = "Error occurred while creating the product";
    public static final String PRODUCTS_NOT_FOUND = "Products not found!";
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product not found with id ";

    public static final String NEGATIVE_NOT_ALLOWED = "Negative numbers are not allowed";

    public static String requiredParameterMessage(String parameterName) {
        return "Required parameter '" + parameterName + "' is null or missing";
    }
}
