package com.sparkplug.sparkplugbackend.messaging.dto.response;

import com.sparkplug.sparkplugbackend.user.model.responseDto.UserResponseDto;

import java.util.List;
import java.util.UUID;

public class ChatWithMessagesResponse {
    private List<MessageResponseDto> messages;
    private UUID chatId;
    private UUID postingId;
    private UserResponseDto seller;
    private UserResponseDto buyer;

    public ChatWithMessagesResponse(List<MessageResponseDto> messages, UUID chatId, UUID postingId, UserResponseDto seller, UserResponseDto buyer) {
        this.messages = messages;
        this.chatId = chatId;
        this.postingId = postingId;
        this.seller = seller;
        this.buyer = buyer;
    }

    public void setMessages(List<MessageResponseDto> messages) {
        this.messages = messages;
    }

    public UUID getPostingId() {
        return postingId;
    }

    public void setPostingId(UUID postingId) {
        this.postingId = postingId;
    }

    public List<MessageResponseDto> getMessages() {
        return messages;
    }

    public void setMessage(List<MessageResponseDto> messages) {
        this.messages = messages;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UserResponseDto getSeller() {
        return seller;
    }

    public void setSeller(UserResponseDto seller) {
        this.seller = seller;
    }

    public UserResponseDto getBuyer() {
        return buyer;
    }

    public void setBuyer(UserResponseDto buyer) {
        this.buyer = buyer;
    }
}
