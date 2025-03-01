package com.sparkplug.sparkplugbackend.posting.dto.response;

import java.util.UUID;

public class TransmissionResponseDto {

    private UUID id;
    private String gearboxType;
    private int numberOfGears;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
    }

    public int getNumberOfGears() {
        return numberOfGears;
    }

    public void setNumberOfGears(int numberOfGears) {
        this.numberOfGears = numberOfGears;
    }
}
