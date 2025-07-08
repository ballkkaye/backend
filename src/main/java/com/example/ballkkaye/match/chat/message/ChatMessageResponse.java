package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.common.enums.ChatConnectedType;
import lombok.Data;

public class ChatMessageResponse {

    @Data
    public static class DTO {
        private Integer chatRoomId;
        private Integer senderId;
        private String senderName;
        private String message;
        private ChatConnectedType messageType;  // e.g., "TALK", "ENTER", "LEAVE"

        public DTO(Integer roomId, Integer senderId, String senderName, String message, ChatConnectedType messageType) {
            this.chatRoomId = roomId;
            this.senderId = senderId;
            this.senderName = senderName;
            this.message = message;
            this.messageType = messageType;
        }
    }
}
