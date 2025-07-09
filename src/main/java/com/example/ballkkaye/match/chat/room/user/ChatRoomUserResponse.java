package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.user.User;
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

    @Data
    public static class ProfileImgDTO {
        private String imgUrl;
        private Integer userId;
        private String nickName;

        public ProfileImgDTO(User user) {
            this.imgUrl = user.getProfileUrl();
            this.userId = user.getId();
            this.nickName = user.getNickname();
        }
    }
}
