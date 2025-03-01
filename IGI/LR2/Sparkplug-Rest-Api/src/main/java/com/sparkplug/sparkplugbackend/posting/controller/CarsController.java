package com.sparkplug.sparkplugbackend.posting.controller;

import com.sparkplug.sparkplugbackend.posting.mapper.CarMapper;
import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.dto.response.CarResponseDto;
import com.sparkplug.sparkplugbackend.posting.service.CarsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("cars/")
public class CarsController {

    private final CarsService carsService;
    private final CarMapper mapper;

    public CarsController(
            CarsService carsService,
            CarMapper mapper) {
        this.carsService = carsService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<CarResponseDto> getAll(){
        List<Car> cars = carsService.getAll();
        //RECURSION IF NOT CONVERTED
        return cars.stream().map(mapper::entityToResponseDto).toList();
    }

    @GetMapping("/{id}")
    public CarResponseDto getById(@PathVariable UUID id){

        return mapper.entityToResponseDto(carsService.get(id));
    }

}
