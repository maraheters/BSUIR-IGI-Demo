package com.sparkplug.sparkplugbackend.posting.dto.response;

import java.util.UUID;

public class ManufacturerResponseDto {

    private UUID id;
    private String name;
    private String country;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
