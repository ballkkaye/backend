package com.example.ballkkaye.match;

import com.example.ballkkaye.match.chat.room.ChatRoomResponse;
import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class MatchResponse {

    @Data
    public static class SaveDTO {
        private ChatRoomResponse.DTO chatRoom;
        private DTO match;

        public SaveDTO(ChatRoomResponse.DTO chatRoom, DTO match) {
            this.chatRoom = chatRoom;
            this.match = match;
        }
    }

    @Data
    public static class DTO {
        private Integer id;
        private String userNickname;
        private String userTeamName;
        private Integer chatRoomId;
        private String title;
        private String content;
        private String createdAt;

        public DTO(Match match) {
            this.id = match.getId();
            this.userNickname = match.getUser().getNickname();
            this.userTeamName = match.getUser().getTeam() == null ? null : match.getUser().getTeam().getTeamName();
            this.chatRoomId = match.getChatRoom().getId();
            this.title = match.getTitle();
            this.content = match.getContent();
            Timestamp timestamp = match.getCreatedAt();
            if (timestamp != null) {
                this.createdAt = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
        }
    }
}
