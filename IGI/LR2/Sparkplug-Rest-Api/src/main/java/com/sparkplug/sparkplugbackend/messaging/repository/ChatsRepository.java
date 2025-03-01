package com.sparkplug.sparkplugbackend.messaging.repository;

import com.sparkplug.sparkplugbackend.messaging.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatsRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c WHERE c.seller.id = :userId OR c.buyer.id = :userId")
    List<Chat> findAllChatsByUserId(@Param("userId") UUID userId);
}
