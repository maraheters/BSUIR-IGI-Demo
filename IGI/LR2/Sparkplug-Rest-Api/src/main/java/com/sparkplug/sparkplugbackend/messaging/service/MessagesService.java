package com.sparkplug.sparkplugbackend.messaging.service;

import com.sparkplug.sparkplugbackend.exception.BadRequestException;
import com.sparkplug.sparkplugbackend.messaging.model.Chat;
import com.sparkplug.sparkplugbackend.messaging.model.Message;
import com.sparkplug.sparkplugbackend.messaging.repository.ChatsRepository;
import com.sparkplug.sparkplugbackend.messaging.repository.MessagesRepository;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessagesService {

    private final MessagesRepository messagesRepository;
    private final SparkplugUsersRepository sparkplugUsersRepository;
    private final ChatsRepository chatsRepository;

    public MessagesService(
            MessagesRepository messagesRepository,
            SparkplugUsersRepository sparkplugUsersRepository,
            ChatsRepository chatsRepository
    ) {
        this.messagesRepository = messagesRepository;
        this.sparkplugUsersRepository = sparkplugUsersRepository;
        this.chatsRepository = chatsRepository;
    }

    public List<Message> getMessagesByChatId(UUID chatId, UUID userId) {
        Chat chat = getChatOrThrow(chatId);

        if (!isUserInChat(userId, chat)) {
            throw new AccessDeniedException("You do not have access to this chat");
        }

        return chat.getMessages();
    }

    public LocalDateTime saveMessage(Message message, UUID senderId, UUID chatId) {
        SparkplugUser sender = getUserOrThrow(senderId);
        Chat chat = getChatOrThrow(chatId);

        if (!isUserInChat(senderId, chat)) {
            throw new AccessDeniedException("You do not have access to this chat");
        }

        message.setSender(sender);
        message.setChat(chat);
        message.setCreatedAt(LocalDateTime.now());
        message.setIsRead(false);

        return messagesRepository.save(message).getCreatedAt();
    }

    private static boolean isUserInChat(UUID userId, Chat chat) {
        return userId.equals(chat.getBuyer().getId()) || userId.equals(chat.getSeller().getId());
    }

    private Chat getChatOrThrow(UUID chatId) {
        return chatsRepository.findById(chatId)
                .orElseThrow(() -> new BadRequestException("Chat with id + '" + chatId + "' not found"));
    }

    private SparkplugUser getUserOrThrow(UUID userId) {
        return sparkplugUsersRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User with id '" + userId + "' not found"));
    }
}
