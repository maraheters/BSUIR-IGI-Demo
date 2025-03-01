package com.sparkplug.sparkplugbackend.posting.dto.request;

import java.util.List;

public class ImageInfoRequestDto {

    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
