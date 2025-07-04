package com.example.notification_service.emailService;

import com.example.notification_service.dto.NotificationPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order-completed", groupId = "notification-group")
    public void consume(NotificationPayload payload) {
        emailService.sendNotification(payload.email(), payload.message());
    }
}

