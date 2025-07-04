package com.example.ballkkaye.match.chat.room;

import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

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
}
