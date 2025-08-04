package com.albuquerque.map_market_v1.controllers;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestPatchShelvingUnitDto;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestShelvingUnitDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.services.ShelvingUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelving-unit")
public class ShelvingUnitController {

    private final ShelvingUnitService service;

    public ShelvingUnitController(ShelvingUnitService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ShelvingUnitDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelvingUnitDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ShelvingUnitDto> create(@RequestBody RequestShelvingUnitDto data) {
        return ResponseEntity.status(201).body(service.create(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelvingUnitDto> update(@PathVariable Long id, @RequestBody RequestShelvingUnitDto data) {
        return ResponseEntity.ok(service.update(id, data));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShelvingUnitDto> patch(@PathVariable Long id, @RequestBody RequestPatchShelvingUnitDto data) {
        return ResponseEntity.ok(service.patch(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
