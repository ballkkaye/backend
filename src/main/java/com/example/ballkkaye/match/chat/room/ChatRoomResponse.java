package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserResponse;
import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatRoomResponse {

    @Data
    public static class DTO {
        private Integer id;
        private Integer gameId;
        private String homeTeamName;
        private String awayTeamName;
        private String matchTitle;
        private Integer preferredTeamId;
        private String preferredTeamName;
        private Integer maxParticipants;
        private String preferredGender;
        private String preferredAge;
        private Boolean isSameTeam;
        private String createdAt;

        public DTO(ChatRoom chatRoom) {
            this.id = chatRoom.getId();
            this.gameId = chatRoom.getGame().getId();
            this.homeTeamName = chatRoom.getGame().getHomeTeam().getTeamName();
            this.awayTeamName = chatRoom.getGame().getAwayTeam().getTeamName();
            this.matchTitle = homeTeamName + " vs " + awayTeamName;
            this.preferredTeamId = chatRoom.getTeam().getId();
            this.preferredTeamName = chatRoom.getTeam().getTeamName();
            this.maxParticipants = chatRoom.getMaxParticipants();
            this.preferredGender = chatRoom.getPreferredGender().getLabel();
            this.preferredAge = chatRoom.getPreferredAge().getName();
            this.isSameTeam = chatRoom.getIsSameTeam();
            Timestamp timestamp = chatRoom.getCreatedAt();
            if (timestamp != null) {
                this.createdAt = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
        }
    }

    @Data
    public static class DeleteDTO {
        private DeleteStatus deleteStatus;

        public DeleteDTO(DeleteStatus deleteStatus) {
            this.deleteStatus = deleteStatus;
        }
    }

    @Data
    public static class ItemDTO {
        private Integer chatRoomId;
        private String chatRoomTitle;
        private Boolean isChatRoomOwner;
        private String relativeTime;
        private String content;
        private List<ChatRoomUserResponse.ProfileImgDTO> userProfileImgs;

        public ItemDTO(Integer chatRoomId, String chatRoomTitle, String relativeTime, String content, List<ChatRoomUserResponse.ProfileImgDTO> imgUrl, Boolean isChatRoomOwner) {
            this.chatRoomId = chatRoomId;
            this.chatRoomTitle = chatRoomTitle;
            this.relativeTime = relativeTime;
            this.content = content;
            this.userProfileImgs = imgUrl;
            this.isChatRoomOwner = isChatRoomOwner;
        }
    }
}
