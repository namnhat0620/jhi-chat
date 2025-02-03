package com.nam.chat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Message")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    private String id;

    private String sentToLogin;
    private String sentFromLogin;
    private String content;
    private String mediaUrl;
    private String timestamp;
    private MessageType messageType;
    private Long groupId;

    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO
    }
}
