package com.sparkplug.sparkplugbackend.posting.dto.request;

import java.math.BigDecimal;

public class EngineRequestDto {
    private int power;
    private int torque;
    private String type;
    private String fuelType;
    private BigDecimal displacement;

    public BigDecimal getDisplacement() {
        return displacement;
    }

    public void setDisplacement(BigDecimal displacement) {
        this.displacement = displacement;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getTorque() {
        return torque;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

}
