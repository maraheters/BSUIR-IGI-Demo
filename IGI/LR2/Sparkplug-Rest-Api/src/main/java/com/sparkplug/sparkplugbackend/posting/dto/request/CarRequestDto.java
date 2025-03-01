package com.sparkplug.sparkplugbackend.posting.dto.request;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CarRequestDto {

    private String drivetrain;
    private String categoryName;
    private String model;
    private int Mileage;
    private int Year;
    private String color;
    private EngineRequestDto engine;
    private TransmissionRequestDto transmission;
    private ManufacturerRequestDto manufacturer;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMileage() {
        return Mileage;
    }

    public void setMileage(int mileage) {
        Mileage = mileage;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EngineRequestDto getEngine() {
        return engine;
    }

    public void setEngine(EngineRequestDto engine) {
        this.engine = engine;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(String drivetrain) {
        this.drivetrain = drivetrain;
    }

    public TransmissionRequestDto getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionRequestDto transmission) {
        this.transmission = transmission;
    }

    public ManufacturerRequestDto getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerRequestDto manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
