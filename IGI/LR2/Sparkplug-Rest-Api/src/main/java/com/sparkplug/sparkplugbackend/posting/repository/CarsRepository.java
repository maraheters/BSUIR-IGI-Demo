package com.sparkplug.sparkplugbackend.posting.repository;

import com.sparkplug.sparkplugbackend.posting.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CarsRepository extends JpaRepository<Car, UUID> {

    @Query("SELECT c.model FROM Car c WHERE c.manufacturer.name = :manufacturerName")
    List<String> findAllModelsByManufacturer(String manufacturerName);
}
