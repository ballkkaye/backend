package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.Region;
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
        private Gender preferredGender;
        private Age preferredAge;
        private Region preferredRegion;
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
            this.preferredGender = chatRoom.getPreferredGender();
            this.preferredAge = chatRoom.getPreferredAge();
            this.preferredRegion = chatRoom.getPreferredRegion();
            this.isSameTeam = chatRoom.getIsSameTeam();
            Timestamp timestamp = chatRoom.getCreatedAt();
            if (timestamp != null) {
                this.createdAt = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
        }
    }
}
