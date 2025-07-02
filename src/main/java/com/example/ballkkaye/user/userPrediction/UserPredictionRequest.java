package com.example.ballkkaye.user.userPrediction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserPredictionRequest {

    @Data
    @NoArgsConstructor
    public static class DTO {
        private Integer gameId;
        private Integer userChoiceTeamId;

        public DTO(Integer gameId, Integer userChoiceTeamId) {
            this.gameId = gameId;
            this.userChoiceTeamId = userChoiceTeamId;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SaveDTO {
        private Integer userId;
        private List<DTO> userPredictions;

        public SaveDTO(Integer userId, List<DTO> userPredictions) {
            this.userId = userId;
            this.userPredictions = userPredictions;
        }
    }
}
