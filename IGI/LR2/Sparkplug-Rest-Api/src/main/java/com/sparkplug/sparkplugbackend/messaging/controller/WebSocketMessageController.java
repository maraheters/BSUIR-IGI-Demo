package com.sparkplug.sparkplugbackend.messaging.controller;

import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageResponseDto;
import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageWithChatIdResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class WebSocketMessageController {

    @MessageMapping("/send/{chatId}")
    @SendTo("/topic/messages/{userId}")
    public MessageWithChatIdResponse testMessage(
            @PathVariable("chatId") UUID chatId,
            MessageResponseDto message
    ) {
        return new MessageWithChatIdResponse(chatId, message);
    }
}
