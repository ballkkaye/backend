package com.example.ballkkaye.user.userPrediction;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

public class UserPredictionResponse {

    @Data
    public static class SaveDTO {
        private Integer id;
        private Integer userId;
        private Integer gameId;
        private Timestamp createdAt;

        public SaveDTO(UserPrediction userPrediction) {
            this.id = userPrediction.getId();
            this.userId = userPrediction.getUser().getId();
            this.gameId = userPrediction.getGame().getId();
            this.createdAt = userPrediction.getCreatedAt();
        }
    }

    @Data
    public static class TodayListDTO {
        private Integer gameId;
        private TeamInfo homeTeam;
        private TeamInfo awayTeam;
        private Integer userChoiceTeamId;
        private ChoicePercentage choicePer;
        private String result; // "O", "X", null
    }

    @Data
    public static class TeamInfo {
        private Integer teamId;
        private String teamName;
        private Integer score; // 경기 종료 후만 노출
        private String logoImgUrl;
    }

    @Data
    public static class ChoicePercentage {
        private Integer home;
        private Integer away;
    }

    @Data
    public static class TodayListResponse {
        private List<TodayListDTO> userPredictions;

        public TodayListResponse(List<TodayListDTO> userPredictions) {
            this.userPredictions = userPredictions;
        }
    }
}