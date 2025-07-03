package com.example.ballkkaye.match;

import com.example.ballkkaye.match.chat.room.ChatRoomResponse;
import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @Data
    public static class ListDTO {
        private String selectedGender;
        private String selectedAge;
        private String selectedRegion;
        private List<Item> matches;

        public ListDTO(String selectedGender, String selectedAge, String selectedRegion, List<Item> matches) {
            this.selectedGender = selectedGender;
            this.selectedAge = selectedAge;
            this.selectedRegion = selectedRegion;
            this.matches = matches;
        }
    }

    @Data
    public static class Item {
        private String gameTitle;
        private String title;
        private String gender;
        private String age;
        private String region;
        private Boolean isSameTeam;
        private String participationInfo;
        private String relativeTime;

        public Item(Match match, String participationInfo, String relativeTime) {
            this.gameTitle = match.getChatRoom().getGame().getHomeTeam().getTeamName() + " vs " + match.getChatRoom().getGame().getAwayTeam().getTeamName();
            ;
            this.title = match.getTitle();
            this.gender = match.getChatRoom().getPreferredGender().getLabel();
            this.age = match.getChatRoom().getPreferredAge().getName();
            this.region = match.getChatRoom().getPreferredRegion().getName();
            this.isSameTeam = match.getChatRoom().getIsSameTeam();
            this.participationInfo = participationInfo + "/" + match.getChatRoom().getMaxParticipants().toString();
            this.relativeTime = relativeTime;
        }
    }
}
