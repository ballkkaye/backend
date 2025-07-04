package com.example.ballkkaye.user.userPrediction;

import lombok.Data;

public class UserPredictionRequest {

    @Data
    public static class SaveDTO {
        private Integer gameId;
        private Integer userChoiceTeamId;
    }
}
