package com.albuquerque.map_market_v1.controllers;

import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.services.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{locationId}/product/{productId}")
    public ResponseEntity<LocationDto> subscribingProduct(@PathVariable Long locationId, @PathVariable Long productId) {
        return ResponseEntity.ok(service.subscribingProduct(locationId, productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> unsubscribingProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.unsubscribingProduct(id));
    }
}
