package com.example.notification_service.dto;

public record NotificationPayload(
        String email,
        String message
) {
}
