package com.sparkplug.sparkplugbackend.security.dto;

public record AuthRequestDto (
        String username,
        String password
) {}