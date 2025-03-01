package com.sparkplug.sparkplugbackend.user.model.responseDto;

import java.util.List;
import java.util.UUID;

public class UserResponseDto {
    private UUID id;
    private String username;
    private String profilePicture;
    private List<UUID> postingIds;

    public UserResponseDto(UUID id, String username, String profilePicture, List<UUID> postingIds) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.postingIds = postingIds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UUID> getPostingIds() {
        return postingIds;
    }

    public void setPostingIds(List<UUID> postingIds) {
        this.postingIds = postingIds;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
