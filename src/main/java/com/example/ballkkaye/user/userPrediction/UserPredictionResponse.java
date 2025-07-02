package com.example.ballkkaye.user.userPrediction;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

public class UserPredictionResponse {

    @Data
    public static class SaveDTO {
        private Integer id;
        private Integer gameId;
        private Integer userChoiceTeamId;
        private Timestamp createdAt;

        public SaveDTO(UserPrediction userPrediction) {
            this.id = userPrediction.getId();
            this.gameId = userPrediction.getGame().getGame().getId();
            this.userChoiceTeamId = userPrediction.getUserChoiceTeam() == null ? null : userPrediction.getUserChoiceTeam().getId();
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

    @Data
    public static class SaveListDTO {
        private Integer userId;
        private List<SaveDTO> userPredictions;

        public SaveListDTO(Integer userId, List<SaveDTO> userPredictions) {
            this.userId = userId;
            this.userPredictions = userPredictions;
        }
    }

}