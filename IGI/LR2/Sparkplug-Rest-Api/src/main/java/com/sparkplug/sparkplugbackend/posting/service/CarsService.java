package com.sparkplug.sparkplugbackend.posting.service;

import com.sparkplug.sparkplugbackend.exception.ResourceNotFoundException;
import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.model.Category;
import com.sparkplug.sparkplugbackend.posting.model.Manufacturer;
import com.sparkplug.sparkplugbackend.posting.repository.CarsRepository;
import com.sparkplug.sparkplugbackend.posting.repository.CategoriesRepository;
import com.sparkplug.sparkplugbackend.posting.repository.ManufacturersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarsService {

    private final CarsRepository carsRepository;
    private final ManufacturersRepository manufacturersRepository;
    private final CategoriesRepository categoriesRepository;

    public CarsService(
            CarsRepository carsRepository,
            ManufacturersRepository manufacturersRepository,
            CategoriesRepository categoriesRepository) {
        this.carsRepository = carsRepository;
        this.manufacturersRepository = manufacturersRepository;
        this.categoriesRepository = categoriesRepository;
    }

    public List<Car> getAll() {
        return carsRepository.findAll();
    }

    @Transactional
    public Car get(UUID id) {
        return carsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with id '" + id + "' not found."));
    }

    @Transactional
    public UUID save(Car car) {
        getOrCreateCategory(car);
        getOrCreateManufacturer(car);

        carsRepository.save(car);
        return car.getId();
    }

    public UUID update(UUID id, Car updatedCar) {
        Car existingCar = carsRepository.findById(id) //throwing RuntimeException because car should exist already
                .orElseThrow(() -> new RuntimeException("Car with id '" + id + "' not found."));

        getOrCreateCategory(updatedCar);
        getOrCreateManufacturer(updatedCar);

        updatedCar.setId(id);
        updatedCar.getEngine().setId(existingCar.getEngine().getId());
        updatedCar.getTransmission().setId(existingCar.getTransmission().getId());

        return carsRepository.save(updatedCar).getId();
    }

    private void getOrCreateCategory(Car car) {
        Optional<Category> existingCategory = categoriesRepository.findByName(car.getCategory().getName());

        if (existingCategory.isPresent()) {
            car.setCategory(existingCategory.get());
        } else {
            categoriesRepository.save(car.getCategory());
        }
    }

    private void getOrCreateManufacturer(Car car) {
        Optional<Manufacturer> existingManufacturer = manufacturersRepository.findByName(car.getManufacturer().getName());

        if (existingManufacturer.isPresent()) {
            car.setManufacturer(existingManufacturer.get());
        } else {
            manufacturersRepository.save(car.getManufacturer());
        }
    }

    @Transactional
    public void delete(UUID id) {
        carsRepository.deleteById(id);
    }
}
