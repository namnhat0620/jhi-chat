package com.nam.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener
        implements ApplicationListener<org.springframework.context.event.ContextRefreshedEvent> {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(org.springframework.context.event.ContextRefreshedEvent event) {
        // Handle the event when WebSocket session is connected or disconnected.
        messagingTemplate.getUserDestinationPrefix();
        // Add logic to track user connections/disconnections and log them
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();
        log.info("User connected: {}", user);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();
        log.info("User disconnected: {}", user);
    }
}
