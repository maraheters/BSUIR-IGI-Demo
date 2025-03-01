package com.sparkplug.sparkplugbackend.posting.model;

import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posting")
public class Posting {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private LocalDateTime creationDate;

    @Column
    private BigDecimal price;

    @Column
    @Size(max = 2048)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private SparkplugUser creator;

    @ManyToMany(mappedBy = "wishlistedPostings")  // This is the inverse side
    private List<SparkplugUser> wishlisters = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_info_id")
    private ImageInfo images;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public @Size(max = 2048) String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 2048) String description) {
        this.description = description;
    }

    public SparkplugUser getCreator() {
        return creator;
    }

    public void setCreator(SparkplugUser creator) {
        this.creator = creator;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ImageInfo getImages() {
        return images;
    }

    public void setImages(ImageInfo images) {
        this.images = images;
    }

    public List<SparkplugUser> getWishlisters() {
        return wishlisters;
    }

    public void setWishlisters(List<SparkplugUser> wishlisters) {
        this.wishlisters = wishlisters;
    }
}
