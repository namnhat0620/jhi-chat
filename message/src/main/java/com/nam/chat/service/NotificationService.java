package com.nam.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.nam.chat.domain.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String username, Message message) {
        log.info("Preparing to send message to {} with payload {}", username, message);

        try {
            messagingTemplate.convertAndSendToUser(username, "/chat", message);
            messagingTemplate.convertAndSendToUser("anonymousUser", "/chat", message);
            messagingTemplate.convertAndSendToUser("user", "/chat", message);
            messagingTemplate.convertAndSendToUser("admin", "/chat", message);
            log.info("Message sent successfully to {}", username);
        } catch (Exception e) {
            log.error("Failed to send message to {}. Error: {}", username, e.getMessage());
        }
    }
}
