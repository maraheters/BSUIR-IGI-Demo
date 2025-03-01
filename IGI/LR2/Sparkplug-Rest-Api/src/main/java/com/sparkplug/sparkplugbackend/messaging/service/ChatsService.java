package com.sparkplug.sparkplugbackend.messaging.service;

import com.sparkplug.sparkplugbackend.exception.BadRequestException;
import com.sparkplug.sparkplugbackend.messaging.mapper.MessageMapper;
import com.sparkplug.sparkplugbackend.messaging.model.Chat;
import com.sparkplug.sparkplugbackend.messaging.dto.response.ChatWithMessagesResponse;
import com.sparkplug.sparkplugbackend.messaging.dto.response.MessageResponseDto;
import com.sparkplug.sparkplugbackend.messaging.repository.ChatsRepository;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.user.mapper.UserMapper;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.posting.repository.PostingsRepository;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ChatsService {

    private final ChatsRepository chatsRepository;
    private final SparkplugUsersRepository usersRepository;
    private final PostingsRepository postingsRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public ChatsService(
            ChatsRepository chatsRepository,
            SparkplugUsersRepository usersRepository,
            PostingsRepository postingsRepository,
            MessageMapper messageMapper,
            UserMapper userMapper
    ) {
        this.chatsRepository = chatsRepository;
        this.usersRepository = usersRepository;
        this.postingsRepository = postingsRepository;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    public UUID initializeChat(UUID senderId, UUID postingId) {
        Posting posting = postingsRepository.findById(postingId)
                .orElseThrow(() -> new BadRequestException("Posting with id '" + postingId + "'  not found"));

        SparkplugUser seller = posting.getCreator();
        if (seller.getId().equals(senderId)) {
            throw new BadRequestException("You may not initialize chat with yourself.");
        }

        SparkplugUser buyer = getUserOrThrow(senderId);

        Chat chat = new Chat();
        chat.setBuyer(buyer);
        chat.setSeller(seller);
        chat.setPosting(posting);

        return chatsRepository.save(chat).getId();
    }

    public UUID getOtherUserId(UUID senderId, UUID chatId) {
        Chat chat = getChatOrThrow(chatId);
        UUID buyerId = chat.getBuyer().getId();
        UUID sellerId = chat.getSeller().getId();

        return senderId.equals(buyerId) ? sellerId : buyerId;
    }

    public List<ChatWithMessagesResponse> getAllChatsByUserId(UUID userId) {
        List<Chat> allChats = chatsRepository.findAllChatsByUserId(userId);

        return allChats.stream()
                .filter(chat -> !chat.getMessages().isEmpty())
                .sorted(Comparator.comparing( //chats with most recent messages will be in the end
                        chat -> chat.getMessages().getLast().getCreatedAt()
                ))
                .map(c -> new ChatWithMessagesResponse(
                        c.getMessages().stream()
                                .map(messageMapper::entityToResponseDto)
                                .sorted(Comparator.comparing(MessageResponseDto::getCreatedAt))
                                .toList(),
                        c.getId(),
                        c.getPosting().getId(),
                        userMapper.entityToResponseDto(c.getSeller()),
                        userMapper.entityToResponseDto(c.getBuyer())
                        )
                ).toList();
    }

    private Chat getChatOrThrow(UUID chatId) {
        return chatsRepository.findById(chatId)
                .orElseThrow(() -> new BadRequestException("Chat with id + '" + chatId + "' not found"));
    }

    private SparkplugUser getUserOrThrow(UUID userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User with id '" + userId + "' not found"));
    }
}
