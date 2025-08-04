package com.albuquerque.map_market_v1.controllers;

import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.services.MapMarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/map")
public class MapMarketController {

    private final MapMarketService service;

    public MapMarketController(MapMarketService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> findRegisteredProducts() {
        return ResponseEntity.ok(service.findRegisteredProducts());
    }

    @GetMapping("/shelvings")
    public ResponseEntity<List<ShelvingUnitDto>> findUnitsWithAssignedProducts() {
        return ResponseEntity.ok(service.findUnitsWithAssignedProducts());
    }

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDto>> findByProductIsNotNull() {
        return ResponseEntity.ok(service.findByProductIsNotNull());
    }

    @GetMapping("/location/product/{id}")
    public ResponseEntity<LocationDto> findLocationByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findLocationByProductId(id));
    }
}
