package com.sparkplug.sparkplugbackend.messaging.repository;

import com.sparkplug.sparkplugbackend.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatId(UUID chatId);
}
