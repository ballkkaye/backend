package com.example.ballkkaye.match.chat.room.user;

import lombok.Data;

public class ChatRoomUserRequest {

    @Data
    public static class AuthDTO {
        private String type;     // "AUTH"
        private String token;    // JWT
        private Integer roomId;  // 채팅방 ID
    }
}
