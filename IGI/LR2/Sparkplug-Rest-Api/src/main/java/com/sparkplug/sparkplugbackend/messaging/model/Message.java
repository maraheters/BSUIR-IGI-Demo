package com.sparkplug.sparkplugbackend.messaging.model;

import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Message {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "sender_id")
    @ManyToOne
    private SparkplugUser sender;

    @JoinColumn(name = "chat_id")
    @ManyToOne
    private Chat chat;

    @Column(length = 4095)
    @Size(min = 1, max = 4095)
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private boolean isRead;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SparkplugUser getSender() {
        return sender;
    }

    public void setSender(SparkplugUser sender) {
        this.sender = sender;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public @Size(min = 1, max = 4095) String getContent() {
        return content;
    }

    public void setContent(@Size(min = 1, max = 4095) String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
