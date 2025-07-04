package com.example.ballkkaye.match;

import com.example.ballkkaye.common.enums.DeleteStatus;
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
        private Integer selectedTeamId;
        private String selectedTimeName;
        private List<Item> matches;

        public ListDTO(String selectedGender, String selectedAge, Integer selectedTeamId, String selectedTimeName, List<Item> matches) {
            this.selectedGender = selectedGender;
            this.selectedAge = selectedAge;
            this.selectedTeamId = selectedTeamId;
            this.selectedTimeName = selectedTimeName;
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
        private Integer matchId;

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
            this.matchId = match.getId();
        }
    }

    @Data
    public static class DetailDTO {
        private String gameTitle;
        private String gameDate;
        private String stadiumName;
        private String userNickname;
        private String userTeamName;
        private String userProfileUrl;
        private String relativeTime;
        private String title;
        private String content;
        private String gender;
        private String age;
        private String region;
        private Boolean isSameTeam;
        private String participationInfo;
        private Boolean isLike;
        private Integer likeCount;
        private Boolean isOwner;
        private Integer chatRoomId;

        public DetailDTO(Match match, Boolean isOwner, String relativeTime, Integer likeCount, Boolean isLike, String countUser) {
            this.gameTitle = match.getChatRoom().getGame().getHomeTeam().getTeamName() + " vs " + match.getChatRoom().getGame().getAwayTeam().getTeamName();
            this.gameDate = match.getChatRoom().getGame().getGameTime().toString();
            this.gameDate = match.getChatRoom().getGame().getGameTime()
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.userNickname = match.getUser().getNickname();
            this.userTeamName = match.getUser().getTeam() == null ? null : match.getUser().getTeam().getTeamName();
            this.userProfileUrl = match.getUser().getProfileUrl();
            this.stadiumName = match.getChatRoom().getGame().getStadium().getStadiumName();
            this.relativeTime = relativeTime;
            this.title = match.getTitle();
            this.content = match.getContent();
            this.gender = match.getChatRoom().getPreferredGender().getLabel();
            this.age = match.getChatRoom().getPreferredAge().getName();
            this.region = match.getChatRoom().getPreferredRegion().getName();
            this.isSameTeam = match.getChatRoom().getIsSameTeam();
            this.participationInfo = countUser + "/" + match.getChatRoom().getMaxParticipants().toString();
            this.isLike = isLike;
            this.likeCount = likeCount;
            this.isOwner = isOwner;
            this.chatRoomId = match.getChatRoom().getId();
        }
    }

    @Data
    public static class UpdateDTO {
        private String gameTitle;
        private String gameDate;
        private String stadiumName;
        private String userNickname;
        private String userTeamName;
        private String userProfileUrl;
        private String relativeTime;
        private String title;
        private String content;
        private String gender;
        private String age;
        private String region;
        private Boolean isSameTeam;
        private String participationInfo;
        private Boolean isLike;
        private Integer likeCount;
        private Boolean isOwner;
        private Integer chatRoomId;

        public UpdateDTO(Match match, Boolean isOwner, String relativeTime, Integer likeCount, Boolean isLike, String countUser) {
            this.gameTitle = match.getChatRoom().getGame().getHomeTeam().getTeamName() + " vs " + match.getChatRoom().getGame().getAwayTeam().getTeamName();
            this.gameDate = match.getChatRoom().getGame().getGameTime().toString();
            this.gameDate = match.getChatRoom().getGame().getGameTime()
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.userNickname = match.getUser().getNickname();
            this.userTeamName = match.getUser().getTeam() == null ? null : match.getUser().getTeam().getTeamName();
            this.userProfileUrl = match.getUser().getProfileUrl();
            this.stadiumName = match.getChatRoom().getGame().getStadium().getStadiumName();
            this.relativeTime = relativeTime;
            this.title = match.getTitle();
            this.content = match.getContent();
            this.gender = match.getChatRoom().getPreferredGender().getLabel();
            this.age = match.getChatRoom().getPreferredAge().getName();
            this.region = match.getChatRoom().getPreferredRegion().getName();
            this.isSameTeam = match.getChatRoom().getIsSameTeam();
            this.participationInfo = countUser + "/" + match.getChatRoom().getMaxParticipants().toString();
            this.isLike = isLike;
            this.likeCount = likeCount;
            this.isOwner = isOwner;
            this.chatRoomId = match.getChatRoom().getId();
        }
    }

    @Data
    public static class DeleteDTO {
        private DeleteStatus deleteStatus;

        public DeleteDTO(DeleteStatus deleteStatus) {
            this.deleteStatus = deleteStatus;
        }
    }
}
