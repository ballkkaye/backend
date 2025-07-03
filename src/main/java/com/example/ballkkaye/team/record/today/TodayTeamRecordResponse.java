package com.example.ballkkaye.team.record.today;

import lombok.Data;

public class TodayTeamRecordResponse {

    @Data
    public static class DTO {
        private String teamName;
        private Double gap;
        private Integer winGame;
        private Integer loseGame;
        private Integer tieGame;
        private Integer totalGame;
        private Double winRate;
        private Integer teamRank;
        private String recentTenGame;
        private String streak;
    }
}