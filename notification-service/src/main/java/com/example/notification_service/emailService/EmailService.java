package com.example.notification_service.emailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final Logger logger = Logger.getLogger(EmailService.class.getName());

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(String toEmail, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Order Notification");
        message.setText(body);
        message.setFrom("xzibitcustrouble@gmail.com");
        try{
            mailSender.send(message);
        }catch (Exception e){
            logger.info("Failed to send mail: "+ e.getMessage());
        }
    }
}

