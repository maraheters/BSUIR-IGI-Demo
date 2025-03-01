package com.sparkplug.sparkplugbackend.posting.repository;

import com.sparkplug.sparkplugbackend.posting.model.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostingsRepository extends
        JpaRepository<Posting, UUID>, JpaSpecificationExecutor<Posting> {

    List<Posting> findByCreatorId(UUID id);

    @Query("SELECT p.car.id FROM Posting p WHERE p.id = :postingId")
    Optional<UUID> findCarIdByPostingId(@Param("postingId") UUID postingId);

    @Query("SELECT p.creator.id FROM Posting p WHERE p.id = :postingId")
    Optional<UUID> findOwnerIdByPostingId(@Param("postingId") UUID postingId);
}
