package com.albuquerque.map_market_v1.entities.dtos.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({ "id", "shelving_unit_id", "side", "part", "shelf", "product"})
public class LocationDto implements Serializable {

    private Long id;

    @JsonProperty("shelving_unit_id")
    private Long shelvingUnitId;
    private String side;
    private Long part;
    private Long shelf;
    private ProductDto productDto;

    public LocationDto() {
    }

    public LocationDto(Long id, ShelvingUnitDto shelvingUnit, String side, Long part, Long shelf, ProductDto productResponseDto) {
        this.id = id;
        this.shelvingUnitId = shelvingUnit.getId();
        this.side = side;
        this.part = part;
        this.shelf = shelf;
        this.productDto = productResponseDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShelvingUnitId() {
        return shelvingUnitId;
    }

    public void setShelvingUnitId(Long shelvingUnit) {
        this.shelvingUnitId = shelvingUnit;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Long getPart() {
        return part;
    }

    public void setPart(Long part) {
        this.part = part;
    }

    public Long getShelf() {
        return shelf;
    }

    public void setShelf(Long shelf) {
        this.shelf = shelf;
    }

    public ProductDto getProduct() {
        return productDto;
    }

    public void setProduct(ProductDto productResponseDto) {
        this.productDto = productResponseDto;
    }
}
