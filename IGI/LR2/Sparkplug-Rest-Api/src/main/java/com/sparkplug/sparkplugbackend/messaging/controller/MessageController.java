package com.sparkplug.sparkplugbackend.messaging.controller;

import com.sparkplug.sparkplugbackend.messaging.mapper.MessageMapper;
import com.sparkplug.sparkplugbackend.messaging.model.Message;
import com.sparkplug.sparkplugbackend.messaging.dto.request.MessageRequestDto;
import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageWithChatIdResponse;
import com.sparkplug.sparkplugbackend.messaging.service.ChatsService;
import com.sparkplug.sparkplugbackend.messaging.service.MessagesService;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageMapper messageMapper;
    private final MessagesService messagesService;
    private final ChatsService chatsService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(
            MessageMapper messageMapper,
            MessagesService messagesService,
            ChatsService chatsService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageMapper = messageMapper;
        this.messagesService = messagesService;
        this.chatsService = chatsService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/{chat-id}")
    public ResponseEntity<LocalDateTime> saveMessage(
            @PathVariable("chat-id") UUID chatId,
            @RequestBody MessageRequestDto messageRequestDto,
            @AuthenticationPrincipal SparkplugUserDetails userDetails
    ) {
        Message message = messageMapper.requestDtoToEntity(messageRequestDto);
        LocalDateTime sentAt = messagesService.saveMessage(message, userDetails.getUser().getId(), chatId);
        UUID recipientId = chatsService.getOtherUserId(userDetails.getUser().getId(), chatId);

        messagingTemplate.convertAndSend(
                "/topic/messages/" + recipientId,
                new MessageWithChatIdResponse(chatId, messageMapper.entityToResponseDto(message)));

        return ResponseEntity.ok(sentAt);
    }
}
