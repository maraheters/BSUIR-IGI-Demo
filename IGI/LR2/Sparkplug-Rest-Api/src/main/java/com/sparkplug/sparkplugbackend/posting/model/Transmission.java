package com.sparkplug.sparkplugbackend.posting.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "transmission")
public class Transmission {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String gearboxType;

    @Column
    private int numberOfGears;

    @OneToOne(mappedBy = "transmission")
    private Car car;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
    }

    public int getNumberOfGears() {
        return numberOfGears;
    }

    public void setNumberOfGears(int numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
