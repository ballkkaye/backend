package com.example.ballkkaye.match.chat.message;

import lombok.Data;

public class ChatMessageRequest {
    @Data
    public class ChatMessage {
        private String sender;
        private String content;
        private String roomId;
    }
}
