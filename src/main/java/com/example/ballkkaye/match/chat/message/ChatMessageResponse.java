package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.common.enums.ChatConnectedType;
import lombok.Data;

public class ChatMessageResponse {

    @Data
    public static class DTO {
        private Integer roomId;
        private Integer senderId;
        private String senderName;
        private String message;
        private ChatConnectedType messageType;  // e.g., "TALK", "ENTER", "LEAVE"
    }
}
