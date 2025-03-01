package com.sparkplug.sparkplugbackend.service;

import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.model.Category;
import com.sparkplug.sparkplugbackend.posting.model.Manufacturer;
import com.sparkplug.sparkplugbackend.posting.repository.CarsRepository;
import com.sparkplug.sparkplugbackend.posting.repository.CategoriesRepository;
import com.sparkplug.sparkplugbackend.posting.repository.ManufacturersRepository;
import com.sparkplug.sparkplugbackend.posting.service.CarsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarsServiceTests {

    @InjectMocks
    private CarsService carsService;

    @Mock
    CarsRepository carsRepository;

    @Mock
    ManufacturersRepository manufacturersRepository;

    @Mock
    CategoriesRepository categoriesRepository;

    @Test
    public void CarsService_save_returnsUUID() {
        Car car = new Car();
        Manufacturer manufacturer = new Manufacturer();
        Category category = new Category();
        car.setId(UUID.randomUUID());
        car.setManufacturer(manufacturer);
        car.setCategory(category);

        when(carsRepository.save(Mockito.any(Car.class))).thenReturn(car);
        when(manufacturersRepository.findByName(Mockito.any())).thenReturn(Optional.ofNullable(car.getManufacturer()));
        when(categoriesRepository.findByName(Mockito.any())).thenReturn(Optional.ofNullable(car.getCategory()));

        UUID id = carsService.save(car);

        Assertions.assertEquals(car.getId(), id);
    }
}
