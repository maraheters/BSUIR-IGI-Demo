package com.sparkplug.sparkplugbackend.posting.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class CarResponseDto {

    private UUID id;
    private String drivetrain;
    private String categoryName;
    private String model;
    private int Mileage;
    private int Year;
    private String color;
    private EngineResponseDto engine;
    private TransmissionResponseDto transmission;
    private ManufacturerResponseDto manufacturer;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public EngineResponseDto getEngine() {
        return engine;
    }

    public void setEngine(EngineResponseDto engine) {
        this.engine = engine;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(String drivetrain) {
        this.drivetrain = drivetrain;
    }

    public TransmissionResponseDto getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionResponseDto transmission) {
        this.transmission = transmission;
    }

    public ManufacturerResponseDto getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerResponseDto manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
