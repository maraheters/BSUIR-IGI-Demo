package com.sparkplug.sparkplugbackend.posting.dto.response;

import java.util.List;
import java.util.UUID;

public class ImageInfoResponseDto {

    private UUID id;
    private List<String> urls;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
