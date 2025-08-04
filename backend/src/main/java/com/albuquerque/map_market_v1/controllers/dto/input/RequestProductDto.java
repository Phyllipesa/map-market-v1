package com.albuquerque.map_market_v1.controllers.dto.input;

import java.math.BigDecimal;

public class RequestProductDto {
    private String name;
    private BigDecimal price;

    public RequestProductDto(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public RequestProductDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
