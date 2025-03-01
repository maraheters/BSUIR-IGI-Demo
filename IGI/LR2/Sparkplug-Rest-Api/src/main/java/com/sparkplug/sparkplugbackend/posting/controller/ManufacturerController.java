package com.sparkplug.sparkplugbackend.posting.controller;

import com.sparkplug.sparkplugbackend.posting.dto.response.ManufacturerResponseDto;
import com.sparkplug.sparkplugbackend.posting.mapper.ManufacturerMapper;
import com.sparkplug.sparkplugbackend.posting.service.ManufacturersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturersService service;
    private final ManufacturerMapper mapper;

    public ManufacturerController(
            ManufacturersService service,
            ManufacturerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ManufacturerResponseDto>> getAll() {

        var manufacturers = service.getAll();
        return ResponseEntity.ok(
                manufacturers.stream().map(mapper::entityToResponseDto).toList());
    }

    @GetMapping("/{name}/models")
    public ResponseEntity<List<String>> getModelsByManufacturer(
            @PathVariable("name") String manufacturerName) {

        var models = service.getModelsByManufacturerName(manufacturerName);
        return ResponseEntity.ok(models);
    }
}
