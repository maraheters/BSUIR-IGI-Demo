package com.sparkplug.sparkplugbackend.messaging.controller;

import com.sparkplug.sparkplugbackend.messaging.mapper.MessageMapper;
import com.sparkplug.sparkplugbackend.messaging.dto.response.ChatWithMessagesResponse;
import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageResponseDto;
import com.sparkplug.sparkplugbackend.messaging.service.ChatsService;
import com.sparkplug.sparkplugbackend.messaging.service.MessagesService;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatsService chatsService;
    private final MessagesService messagesService;
    private final MessageMapper messageMapper;

    public ChatController(
            ChatsService chatsService,
            MessagesService messagesService,
            MessageMapper messageMapper
    ) {
        this.chatsService = chatsService;
        this.messagesService = messagesService;
        this.messageMapper = messageMapper;
    }

    @GetMapping
    public ResponseEntity<List<ChatWithMessagesResponse>> getAllChats(
            @AuthenticationPrincipal SparkplugUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                chatsService.getAllChatsByUserId(userDetails.getUser().getId()));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponseDto>> getMessagesByChatId(
            @PathVariable("id") UUID chatId,
            @AuthenticationPrincipal SparkplugUserDetails userDetails) {

        List<MessageResponseDto> messages =
                messagesService.getMessagesByChatId(chatId, userDetails.getUser().getId())
                        .stream()
                        .map(messageMapper::entityToResponseDto).toList();

        return ResponseEntity.ok(messages);
    }

    @PostMapping("/initial/{posting-id}")
    public ResponseEntity<UUID> createChat(
            @PathVariable("posting-id") UUID postingId,
            @AuthenticationPrincipal SparkplugUserDetails userDetails
    ) {
        UUID senderId = userDetails.getUser().getId();
        UUID chatId = chatsService.initializeChat(senderId, postingId);

        return ResponseEntity.ok(chatId);
    }
}

