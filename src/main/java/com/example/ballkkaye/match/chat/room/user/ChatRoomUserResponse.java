package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.common.enums.DeleteStatus;
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

    @Data
    public static class DeleteDTO {
        private String deleteStatus;

        public DeleteDTO() {
            this.deleteStatus = DeleteStatus.DELETED.getLabel();
        }
    }


}
