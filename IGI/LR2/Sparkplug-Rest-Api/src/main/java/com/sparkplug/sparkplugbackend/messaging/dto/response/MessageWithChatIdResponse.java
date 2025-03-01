package com.sparkplug.sparkplugbackend.messaging.dto.response;

import java.util.UUID;

public class MessageWithChatIdResponse {
    private UUID chatId;
    private MessageResponseDto message;

    public MessageWithChatIdResponse(UUID chatId, MessageResponseDto message) {
        this.chatId = chatId;
        this.message = message;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public MessageResponseDto getMessage() {
        return message;
    }

    public void setMessage(MessageResponseDto message) {
        this.message = message;
    }
}
