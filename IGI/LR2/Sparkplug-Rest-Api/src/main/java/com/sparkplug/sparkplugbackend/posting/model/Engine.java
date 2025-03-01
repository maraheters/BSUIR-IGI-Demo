package com.sparkplug.sparkplugbackend.posting.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "engine")
public class Engine {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private int power;

    @Column
    private int torque;

    @Column
    private String type;

    @Column
    private String fuelType;

    @Column
    private BigDecimal displacement;

    @OneToOne(mappedBy = "engine")
    private Car car;

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTorque() {
        return torque;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public String getType() {
        return type;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getDisplacement() {
        return displacement;
    }

    public void setDisplacement(BigDecimal displacement) {
        this.displacement = displacement;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "id=" + id +
                ", power=" + power +
                ", type='" + type + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", car=" + car +
                '}';
    }

}
