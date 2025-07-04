package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.common.enums.PredictionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPredictionResponse {

    // 예측 가능한 경기 리스트 조회용 DTO
    @Data
    public static class TodayGameDTO {
        private Integer gameId;
        private String gameTime;

        private TeamDTO homeTeam;
        private TeamDTO awayTeam;

        private Integer homeVoteRate;
        private Integer awayVoteRate;
    }

    // 공통 팀 정보 DTO
    @Data
    public static class TeamDTO {
        private Integer teamId;
        private String teamName;
        private String logoUrl;
    }

    @Data
    public static class MyPredictionDTO {
        private Integer gameId;
        private String gameTime;

        private TeamDTO homeTeam;
        private TeamDTO awayTeam;

        private Integer homeScore;
        private Integer awayScore;

        private Integer userChoiceTeamId;
        private PredictionStatus predictionStatus;

        private Integer homeVoteRate;
        private Integer awayVoteRate;

        private GameStatus gameStatus;
        private LocalDateTime updatedAt;
    }
}
