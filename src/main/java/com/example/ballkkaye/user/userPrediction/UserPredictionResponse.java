package com.example.ballkkaye.user.userPrediction;

import lombok.Data;

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

    // 유저 예측 결과 조회용 DTO
    @Data
    public static class PredictionResultDTO {
        private Integer gameId;
        private String gameTime;

        private TeamDTO homeTeam;
        private TeamDTO awayTeam;

        private Integer homeScore;
        private Integer awayScore;

        private Integer userVotedTeamId;
        private Boolean userPredictionCorrect;

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
}
