package org.forum.controllers;

import org.forum.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import java.sql.Timestamp;

@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleChatMessage(ChatMessage message) {
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return message;
    }
} 