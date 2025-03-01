package com.sparkplug.sparkplugbackend.posting.repository;

import com.sparkplug.sparkplugbackend.posting.model.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnginesRepository extends JpaRepository<Engine, UUID> {
}
