package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.common.enums.ChatConnectedType;
import lombok.Data;

public class ChatMessageRequest {

    @Data
    public static class DTO {
        private Integer roomId;
        private String message;
        private String accessToken;
        private ChatConnectedType messageType;  // e.g., "TALK", "ENTER", "LEAVE"
    }
}
