package com.sparkplug.sparkplugbackend.messaging.model;

import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Chat {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "seller_id")
    @ManyToOne
    private SparkplugUser seller;

    @JoinColumn(name = "buyer_id")
    @ManyToOne
    private SparkplugUser buyer;

    @JoinColumn(name = "posting_id")
    @ManyToOne
    private Posting posting;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SparkplugUser getSeller() {
        return seller;
    }

    public void setSeller(SparkplugUser seller) {
        this.seller = seller;
    }

    public SparkplugUser getBuyer() {
        return buyer;
    }

    public void setBuyer(SparkplugUser buyer) {
        this.buyer = buyer;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
