package com.albuquerque.map_market_v1.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class LocationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelving_unit_id")
    private ShelvingUnitEntity shelvingUnit;
    private String side;
    private Long part;
    private Long shelf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public LocationEntity() {
    }

    public LocationEntity(ShelvingUnitEntity shelvingUnit, String side, Long part, Long shelf, ProductEntity product) {
        this.shelvingUnit = shelvingUnit;
        this.side = side;
        this.part = part;
        this.shelf = shelf;
        this.product = product;
    }

    public LocationEntity(Long id, ShelvingUnitEntity shelvingUnit, String side, Long part, Long shelf, ProductEntity product) {
        this.id = id;
        this.shelvingUnit = shelvingUnit;
        this.side = side;
        this.part = part;
        this.shelf = shelf;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public ShelvingUnitEntity getShelvingUnit() {
        return shelvingUnit;
    }

    public void setShelvingUnit(ShelvingUnitEntity shelvingUnit) {
        this.shelvingUnit = shelvingUnit;
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

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationEntity locationEntity)) return false;
        return Objects.equals(id, locationEntity.id)
            && Objects.equals(shelvingUnit, locationEntity.shelvingUnit)
            && Objects.equals(side, locationEntity.side)
            && Objects.equals(part, locationEntity.part)
            && Objects.equals(shelf, locationEntity.shelf)
            && Objects.equals(product, locationEntity.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shelvingUnit, side, part, shelf, product);
    }
}
