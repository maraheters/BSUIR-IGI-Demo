package com.sparkplug.sparkplugbackend.posting.dto.response;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PostingResponseDto {

    private UUID id;
    private LocalDateTime creationDate;
    private String creator;
    private UUID creatorId;
    @Size(max = 2048)
    private String description;
    private BigDecimal price;
    private CarResponseDto car;
    private List<String> images;

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

    public @Size(max = 2048) String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 2048) String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public CarResponseDto getCar() {
        return car;
    }

    public void setCar(CarResponseDto car) {
        this.car = car;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
