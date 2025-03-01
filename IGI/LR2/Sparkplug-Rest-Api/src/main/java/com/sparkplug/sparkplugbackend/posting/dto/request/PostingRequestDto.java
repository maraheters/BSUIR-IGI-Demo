package com.sparkplug.sparkplugbackend.posting.dto.request;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PostingRequestDto {

    @Size(max = 2048)
    private String Description;
    private BigDecimal price;
    private CarRequestDto carRequest;

    public @Size(max = 2048) String getDescription() {
        return Description;
    }

    public void setDescription(@Size(max = 2048) String description) {
        Description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CarRequestDto getCarRequest() {
        return carRequest;
    }

    public void setCarRequest(CarRequestDto carRequest) {
        this.carRequest = carRequest;
    }
}
