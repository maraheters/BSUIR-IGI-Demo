package com.sparkplug.sparkplugbackend.posting.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Manufacturer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private String name;

    @Column
    private String country;

    @OneToMany(mappedBy = "manufacturer")
    private List<Car> cars;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
