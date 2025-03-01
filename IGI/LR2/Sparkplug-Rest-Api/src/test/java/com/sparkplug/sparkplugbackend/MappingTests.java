package com.sparkplug.sparkplugbackend;

import com.sparkplug.sparkplugbackend.posting.mapper.*;
import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.mapper.CarMapper;
import com.sparkplug.sparkplugbackend.posting.dto.request.CarRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.request.EngineRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.request.ManufacturerRequestDto;
import com.sparkplug.sparkplugbackend.posting.dto.request.TransmissionRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MappingTests {

    private final CarMapper carMapper = new CarMapperImpl(
            new EngineMapperImpl(),
            new ManufacturerMapperImpl(),
            new TransmissionMapperImpl()
    );


    @Test
    public void carRequestToEntity() {
        CarRequestDto carRequestDto = initCarRequestDto();

        Car car = carMapper.requestDtoToEntity(carRequestDto);
        assertEquals(carRequestDto.getModel(), car.getModel());
        assertEquals(carRequestDto.getMileage(), car.getMileage());
        assertEquals(carRequestDto.getYear(), car.getYear());
        assertEquals(carRequestDto.getColor(), car.getColor());
        assertEquals(carRequestDto.getDrivetrain(), car.getDrivetrain());
        assertEquals(carRequestDto.getCategoryName(), car.getCategory().getName());

        // Check nested objects
        assertEquals(carRequestDto.getEngine().getPower(), car.getEngine().getPower());
        assertEquals(carRequestDto.getEngine().getDisplacement(), car.getEngine().getDisplacement());
        assertEquals(carRequestDto.getEngine().getFuelType(), car.getEngine().getFuelType());
        assertEquals(carRequestDto.getManufacturer().getName(), car.getManufacturer().getName());
        assertEquals(carRequestDto.getTransmission().getGearboxType(), car.getTransmission().getGearboxType());
    }

    private CarRequestDto initCarRequestDto() {
        EngineRequestDto engineRequestDto = new EngineRequestDto();
        engineRequestDto.setPower(300);
        engineRequestDto.setTorque(400);
        engineRequestDto.setType("V8");
        engineRequestDto.setFuelType("Petrol");
        engineRequestDto.setDisplacement(new BigDecimal("5.0"));


        ManufacturerRequestDto manufacturerRequestDto = new ManufacturerRequestDto();
        manufacturerRequestDto.setName("Tesla");
        manufacturerRequestDto.setCountry("USA");

        TransmissionRequestDto transmissionRequestDto = new TransmissionRequestDto();
        transmissionRequestDto.setGearboxType("Automatic");
        transmissionRequestDto.setNumberOfGears(8);

        CarRequestDto carRequestDto = new CarRequestDto();
        carRequestDto.setDrivetrain("drivetrain");
        carRequestDto.setCategoryName("categoryName");
        carRequestDto.setModel("model");
        carRequestDto.setMileage(25);
        carRequestDto.setYear(2020);
        carRequestDto.setColor("color");
        carRequestDto.setEngine(engineRequestDto);
        carRequestDto.setTransmission(transmissionRequestDto);
        carRequestDto.setManufacturer(manufacturerRequestDto);

        return carRequestDto;
    }
}
