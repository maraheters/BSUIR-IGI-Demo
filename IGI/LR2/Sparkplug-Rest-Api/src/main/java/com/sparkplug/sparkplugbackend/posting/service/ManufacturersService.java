package com.sparkplug.sparkplugbackend.posting.service;

import com.sparkplug.sparkplugbackend.posting.model.Manufacturer;
import com.sparkplug.sparkplugbackend.posting.repository.CarsRepository;
import com.sparkplug.sparkplugbackend.posting.repository.ManufacturersRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ManufacturersService {

    private final ManufacturersRepository manufacturersRepository;
    private final CarsRepository carsRepository;

    public ManufacturersService(
            ManufacturersRepository manufacturersRepository,
            CarsRepository carsRepository) {
        this.manufacturersRepository = manufacturersRepository;
        this.carsRepository = carsRepository;
    }

    public List<Manufacturer> getAll() {
        var manufacturers = manufacturersRepository.findAll();
        manufacturers.sort(Comparator.comparing(Manufacturer::getName));

        return manufacturers;
    }

    public List<String> getModelsByManufacturerName(String manufacturerName) {
        var models = carsRepository.findAllModelsByManufacturer(manufacturerName);
        models.sort(String::compareTo);

        return models;
    }
}
