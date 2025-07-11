package com.example.ballkkaye.user.userPrediction;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class UserPredictionRequest {

    @Data
    public static class SaveDTO {
        @NotNull(message = "경기 ID는 필수입니다.")
        private Integer gameId;
        
        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer userChoiceTeamId;
    }
}
