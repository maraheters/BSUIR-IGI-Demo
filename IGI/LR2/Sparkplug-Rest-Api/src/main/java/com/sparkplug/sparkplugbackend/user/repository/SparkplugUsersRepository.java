package com.sparkplug.sparkplugbackend.user.repository;

import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SparkplugUsersRepository extends JpaRepository<SparkplugUser, UUID> {
    Optional<SparkplugUser> findByUsername(String username);
}
