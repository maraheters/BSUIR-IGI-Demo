package com.sparkplug.sparkplugbackend.posting.query;

public record SearchCriteria(
        String key,
        String operation,
        Object value) {}

