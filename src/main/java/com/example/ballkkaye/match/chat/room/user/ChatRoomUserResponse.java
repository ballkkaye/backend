package com.example.ballkkaye.match.chat.room.user;

import lombok.Data;

public class ChatRoomUserResponse {
    @Data
    public static class SaveDTO {
        private Integer chatRoomUserId;
        private Integer chatRoomId;
        private Integer userId;

        public SaveDTO(ChatRoomUser chatRoomUser) {
            this.chatRoomUserId = chatRoomUser.getId();
            this.chatRoomId = chatRoomUser.getChatRoom().getId();
            this.userId = chatRoomUser.getUser().getId();
        }
    }
}
