package com.sparkplug.sparkplugbackend.security.dto;

import java.util.UUID;

public record AuthResponseDto(
        UUID id,
        String username,
        String authority,
        String authToken
){}
