package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.common.enums.ChatConnectedType;
import lombok.Data;

import java.sql.Timestamp;

public class ChatMessageResponse {

    @Data
    public static class DTO {
        private Integer chatRoomId;
        private Integer senderId;
        private String senderName;
        private String message;
        private ChatConnectedType messageType; // e.g., "TALK", "ENTER", "LEAVE"
        private Boolean isOwner;
        private Timestamp createdAt;

        public DTO(Integer roomId, Integer senderId, String senderName, String message, ChatConnectedType messageType, Boolean isOwner, Timestamp createdAt) {
            this.chatRoomId = roomId;
            this.senderId = senderId;
            this.senderName = senderName;
            this.message = message;
            this.messageType = messageType;
            this.isOwner = isOwner;
            this.createdAt = createdAt;
        }
    }
}
