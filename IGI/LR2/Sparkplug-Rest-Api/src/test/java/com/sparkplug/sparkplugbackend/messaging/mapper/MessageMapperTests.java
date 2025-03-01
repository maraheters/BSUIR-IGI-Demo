package com.sparkplug.sparkplugbackend.messaging.mapper;

import com.sparkplug.sparkplugbackend.messaging.model.Chat;
import com.sparkplug.sparkplugbackend.messaging.model.Message;
import com.sparkplug.sparkplugbackend.messaging.dto.request.MessageRequestDto;
import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageResponseDto;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MessageMapperTests {

    private final MessageMapper mapper = new MessageMapperImpl();

    @Test
    public void MessageMapper_RequestDtoToEntity_shouldWork() {
        MessageRequestDto dto = new MessageRequestDto();
        dto.setContent("Hi");

        Message message = mapper.requestDtoToEntity(dto);

        assertEquals(dto.getContent(), message.getContent());
    }

    @Test
    public void MessageMapper_EntityToResponseDto_shouldWork() {
        SparkplugUser u = new SparkplugUser();
        u.setId(UUID.randomUUID());
        u.setUsername("username");
        Chat c = new Chat();
        c.setId(UUID.randomUUID());
        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setContent("Hi");
        message.setIsRead(true);
        message.setCreatedAt(LocalDateTime.now());
        message.setSender(u);
        message.setChat(c);

        MessageResponseDto dto = mapper.entityToResponseDto(message);

        assertEquals(dto.getSenderUsername(), message.getSender().getUsername());
        assertEquals(dto.getCreatedAt(), message.getCreatedAt());
        assertEquals(dto.getSenderId(), message.getSender().getId());
        assertEquals(dto.getContent(), message.getContent());
    }
}
