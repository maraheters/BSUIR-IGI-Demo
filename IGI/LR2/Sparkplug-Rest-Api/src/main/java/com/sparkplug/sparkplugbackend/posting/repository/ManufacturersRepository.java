package com.sparkplug.sparkplugbackend.posting.repository;

import com.sparkplug.sparkplugbackend.posting.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManufacturersRepository extends JpaRepository<Manufacturer, UUID> {

    Optional<Manufacturer> findByName(String name);

}
